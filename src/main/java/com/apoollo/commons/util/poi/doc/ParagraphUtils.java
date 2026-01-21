package com.apoollo.commons.util.poi.doc;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import com.alibaba.fastjson2.JSON;
import com.apoollo.commons.util.LangUtils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * liuyulong
 */
public class ParagraphUtils {


	private static final char[] STARTS = new char[] { '$', '{' };
	private static final char END = '}';

	public static enum MomentType {
		HAVE_START_AND_NOT_END, HALF_START;
	}

	@Getter
	@Setter
	@AllArgsConstructor
	public static class MomentBranch {

		private MomentType momentType;
		private int startRunIndex;
		private XWPFRun startRun;
		private StringBuilder startRunText;
		private int startRunTextIndex;
		private int startsArrayIndex;

		private StringBuilder keyTextPartsVariable;

	}

	public static void compileParagraphs(List<XWPFParagraph> paragraphs, Map<String, Object> paragraphMapping) {
		LangUtils.getStream(paragraphs).forEach(paragraph -> {

			MomentBranch momentBranch = null;
			int runsSize = paragraph.getRuns().size();
			for (int i = 0; i < runsSize; i++) {
				XWPFRun run = paragraph.getRuns().get(i);
				String text = run.getText(0);
				if (StringUtils.isNotBlank(text)) {

					int startIndex = -1;
					int endIndex = -1;
					int startMidIndex = -1;
					int startsArrayIndex = -1;
					boolean changed = false;
					StringBuilder sbText = new StringBuilder(text);

					for (int j = 0; j < sbText.length(); j++) {
						char c = sbText.charAt(j);

						if (null != momentBranch) {

							if (MomentType.HALF_START == momentBranch.getMomentType()) {

								// TODO Half Start Process
								throw new UnsupportedOperationException("未实现：" + MomentType.HALF_START);

							} else if (MomentType.HAVE_START_AND_NOT_END == momentBranch.getMomentType()) {

								momentBranch.getKeyTextPartsVariable().append(c);

								if (END == c) {

									StringBuilder startRunText = momentBranch.getStartRunText();
									String key = startRunText.substring(
											momentBranch.getStartRunTextIndex() + STARTS.length, startRunText.length());
									if (momentBranch.getKeyTextPartsVariable().length() > 1) {

										key += momentBranch.getKeyTextPartsVariable().substring(0,
												momentBranch.getKeyTextPartsVariable().length() - 1);
									}
									Object value = paragraphMapping.get(key);
									if (null != value) {
										// first run replace
										momentBranch.getStartRun().setText(momentBranch.getStartRunText()
												.replace(momentBranch.getStartRunTextIndex(),
														momentBranch.getStartRunText().length(), value.toString())
												.toString(), 0);

										// cleans
										run.setText(sbText.replace(j, j + 1, "").toString(), 0);
										for (int m = momentBranch.getStartRunIndex(); m < j; m++) {
											paragraph.getRuns().get(i).setText("", 0);
										}

										// reset
										momentBranch = null;
										break;
									} else {

										// reset
										j = momentBranch.getStartRunIndex();
										momentBranch = null;
										break;
									}
								} else if (i == runsSize - 1 && j == sbText.length() - 1) {
									throw new RuntimeException("can't find end :" + JSON.toJSONString(momentBranch));
								}
							}
						} else {

							if (-1 == startIndex) {
								if (j - 1 + STARTS.length <= sbText.length() - 1) {
									boolean allTrue = true;
									for (int k = 0; k < STARTS.length; k++) {
										char start = STARTS[k];
										char nextChar = sbText.charAt(j + k);
										if (start != nextChar) {
											allTrue = false;
											break;
										}
									}
									if (allTrue) {
										startIndex = j;
										j = j - 1 + STARTS.length;
										continue;
									}
								} else {

									if (STARTS[0] == c) {

										boolean allTrue = true;
										int k = 1;
										for (; k < sbText.length() - j; k++) {
											char start = STARTS[k];
											char nextChar = sbText.charAt(j + k);
											if (start != nextChar) {
												allTrue = false;
												break;
											}
										}
										if (allTrue) {
											startMidIndex = j + k - 1;
											startsArrayIndex = k;
										}
									}

								}
							} else {

								if (-1 == endIndex) {
									if (END == c) {
										endIndex = j;
										String key = sbText.substring(startIndex + STARTS.length, endIndex);
										Object value = paragraphMapping.get(key);
										if (null != value) {

											// replace string builder text
											String valueString = value.toString();
											sbText.replace(startIndex, endIndex + 1, valueString);

											// calculate j
											j = startIndex - 1 + valueString.length();
											changed = true;
										}
										startIndex = -1;
										endIndex = -1;
									}
								}
							}

							if (changed) {
								run.setText(sbText.toString(), 0);
							}

							if (startIndex >= 0) {
								// have start , not end
								if (j == sbText.length() - 1 && -1 == endIndex) {
									momentBranch = new MomentBranch(MomentType.HAVE_START_AND_NOT_END, i, run, sbText,
											startIndex, startsArrayIndex, new StringBuilder());
								}
							} else {

								// half start
								if (startMidIndex >= 0) {
									momentBranch = new MomentBranch(MomentType.HALF_START, i, run, sbText,
											startMidIndex, startsArrayIndex, new StringBuilder());
								}
							}
						}
					}
				}
			}
		});
	}
}
