/**
 * 
 */
package com.apoollo.commons.util.path;

import java.io.File;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;

import com.apoollo.commons.util.LangUtils;
import com.apoollo.commons.util.model.PathFile;

/**
 * liuyulong
 */
public interface PathJoiner {

	public static final String BLANK = "";

	public String getPathSplitor();

	public static PathJoiner getPathJoiner() {
		PathJoiner pathJoiner = null;
		if (File.separator.equals("/")) {
			pathJoiner = LeftFallingPathJoinner.getPathJoiner();
		} else if (File.separator.equals("\\")) {
			pathJoiner = RightFallingPathJoinner.getPathJoiner();
		} else {
			throw new RuntimeException("unknow path separator :" + File.separator);
		}
		return pathJoiner;
	}

	public default String getRootStart() {
		return forceProcess(() -> "/", () -> BLANK, () -> BLANK);
	}

	public default String forceProcess(Supplier<String> leftFallingPathSupplier,
			Supplier<String> rightFallingPathSupplier, Supplier<String> defaultSupplier) {
		String pathSplitor = getPathSplitor();
		String result = null;
		if (pathSplitor.equals("/")) {
			result = leftFallingPathSupplier.get();
		} else if (pathSplitor.equals("\\")) {
			result = rightFallingPathSupplier.get();
		} else {
			result = defaultSupplier.get();
		}
		return result;
	}

	public default String forceFilter(String path) {
		return forceProcess(() -> path.replace("\\", "/"), () -> path.replace("/", "\\"), () -> path);
	}

	/**
	 * 转换成为 path/
	 * 
	 * @param path
	 * @return
	 */
	public default String filter2SegmentPath(String path) {
		String ret = null;
		if (StringUtils.isNotBlank(path)) {
			path = forceFilter(path);
			path = StringUtils.trim(path);
			if (!getPathSplitor().equals(path)) {
				if (path.startsWith(getPathSplitor())) {
					path = path.substring(1);
				}

				if (!path.endsWith(getPathSplitor())) {
					path = path.concat(getPathSplitor());
				}
				ret = path;
			}
		}
		if (null == ret) {
			ret = BLANK;
		}
		return ret;
	}

	/**
	 * 转换成 path/name.abc
	 * 
	 * @param path
	 * @return
	 */
	public default String filter2EndFile(String path) {
		if (StringUtils.isNotBlank(path)) {
			path = forceFilter(path);
			path = StringUtils.trim(path);
			if (path.startsWith(getPathSplitor())) {
				path = path.substring(1);
			}
		}

		if (null == path) {
			path = BLANK;
		}
		return path;
	}

	/**
	 * 转换成 ?path/path/
	 * 
	 * @param pathStream
	 * @return
	 */
	public default String joinRootPath(Stream<String> pathStream) {
		return pathStream.reduce(getRootStart(), (result, element) -> {

			return new StringBuilder(result).append(filter2SegmentPath(element)).toString();
		});
	}

	public default String joinRootPath(List<String> pathList) {
		return joinRootPath(LangUtils.getStream(pathList));
	}

	public default String joinRootPath(String... paths) {
		return joinRootPath(LangUtils.getStream(paths));
	}

	public default String joinRootPath(List<String> pathList, String endFile) {
		return new StringBuilder(joinRootPath(pathList)).append(filter2EndFile(endFile)).toString();
	}

	public default String joinRootPath(String root, String endFile) {
		return joinRootPath(LangUtils.toList(root), endFile);
	}

	public default String gerFileName(String name, String extension) {
		String fileName = null;
		if (StringUtils.isNotBlank(name) && StringUtils.isNotBlank(extension)) {
			fileName = name + "." + extension;
		}
		return fileName;
	}
	

	public default PathFile joinToPathFile(String rootPath, List<String> segumentPaths, String name, String extension) {
		String fileName = gerFileName(name, extension);
		String secondHalfSectionPath = joinRootPath(segumentPaths, fileName);
		if (secondHalfSectionPath.startsWith(getPathSplitor())) {
			secondHalfSectionPath = secondHalfSectionPath.substring(1);
		}
		String absolutePath = joinRootPath(rootPath, secondHalfSectionPath);
		String parentPath = StringUtils.substringBeforeLast(absolutePath, getPathSplitor());

		PathFile pathFile = new PathFile();
		pathFile.setAbsolutePath(absolutePath);
		pathFile.setExtension(extension);
		pathFile.setName(name);
		pathFile.setFileName(fileName);
		pathFile.setRootPath(rootPath);
		pathFile.setSecondHalfSectionPath(secondHalfSectionPath);
		pathFile.setParentPath(parentPath);

		return pathFile;
	}

}
