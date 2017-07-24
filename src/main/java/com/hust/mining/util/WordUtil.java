package com.hust.mining.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFonts;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr;

import com.hust.mining.constant.Constant.FONT;

/**
 * 写word文档
 * 
 * @author Chan
 *
 */
public class WordUtil {
	FileOutputStream fos;
	XWPFDocument doc;
	XWPFParagraph para;

	public WordUtil() {
		doc = new XWPFDocument();
	}

	/**
	 * 以filename命名写word
	 * 
	 * @param filename
	 * @throws IOException
	 */
	public void write(String filename) throws IOException {
		fos = new FileOutputStream(new File(filename));
		doc.write(fos);
	}

	/**
	 * 设置font类型、插入一段文本
	 * 
	 * @param content
	 * @param env
	 */
	public void addParaText(String content, Env env) {
		para = doc.createParagraph();
		para.setAlignment(env.getAlignment());
		XWPFRun run = para.createRun();
		run.setText(content);
		run.setFontSize(env.getFontSize());
		run.setBold(env.isBold());
		run.setColor(env.getFontColor());
		CTRPr rpr = run.getCTR().isSetRPr() ? run.getCTR().getRPr() : run.getCTR().addNewRPr();
		CTFonts fonts = rpr.isSetRFonts() ? rpr.getRFonts() : rpr.addNewRFonts();
		fonts.setAscii(env.getFontType());
		fonts.setEastAsia(env.getFontType());
		fonts.setHAnsi(env.getFontType());
		rpr.addNewU();
	}

	/**
	 * 设置文本类型，在已有段落后追加
	 * 
	 * @param content
	 * @param env
	 */
	public void appendParaText(String content, Env env) {
		para.setAlignment(env.getAlignment());
		XWPFRun run = para.createRun();
		run.setText(content);
		run.setFontSize(env.getFontSize());
		run.setBold(env.isBold());
		run.setColor(env.getFontColor());
		CTRPr rpr = run.getCTR().isSetRPr() ? run.getCTR().getRPr() : run.getCTR().addNewRPr();
		CTFonts fonts = rpr.isSetRFonts() ? rpr.getRFonts() : rpr.addNewRFonts();
		fonts.setAscii(env.getFontType());
		fonts.setEastAsia(env.getFontType());
		fonts.setHAnsi(env.getFontType());
	}

	/**
	 * 空格
	 */
	public void setBreak() {
		para = doc.createParagraph();
		XWPFRun run = para.createRun();
		run.addBreak();
	}

	/**
	 * 换页
	 */
	public void setPageBreak() {
		para = doc.createParagraph();
		para.setPageBreak(true);
	}
	
	/**
	 * 设置文本参数：字体大小、字体、字体颜色、加粗与否、标题目录、左对齐居中右对齐
	 * 
	 * @author Chan
	 *
	 */
	public static class Env {
		private int fontSize;
		private String fontType;
		private String fontColor;
		private boolean bold;
		private String style;
		private ParagraphAlignment alignment;

		public Env() {
			// 默认14号华文仿宋灰色未加粗字体
			fontSize = 14;
			fontType = FONT.FANGSONG;
			alignment = ParagraphAlignment.LEFT;
		}

		public Env(Env env) {
			fontSize = env.getFontSize();
			fontType = env.getFontType();
			fontColor = env.getFontColor();
			bold = env.isBold();
			style = env.getStyle();
			alignment = env.getAlignment();
		}

		public Env fontSize(int fontSize) {
			this.fontSize = fontSize;
			return this;
		}

		public Env fontType(String fontType) {
			this.fontType = fontType;
			return this;
		}

		public Env fontColor(String fontColor) {
			this.fontColor = fontColor;
			return this;
		}

		public Env bold(boolean bold) {
			this.bold = bold;
			return this;
		}

		public Env style(String style) {
			this.style = style;
			return this;
		}

		public Env alignment(String margin) {
			if (margin.equals("center")) {
				this.alignment = ParagraphAlignment.CENTER;
			} else if (margin.equals("right")) {
				this.alignment = ParagraphAlignment.RIGHT;
			}
			return this;
		}

		public int getFontSize() {
			return fontSize;
		}

		public String getFontType() {
			return fontType;
		}

		public String getFontColor() {
			return fontColor;
		}

		public boolean isBold() {
			return bold;
		}

		public String getStyle() {
			return style;
		}

		public ParagraphAlignment getAlignment() {
			return alignment;
		}
	}
}
