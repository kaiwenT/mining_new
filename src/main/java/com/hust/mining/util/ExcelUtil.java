package com.hust.mining.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.hust.mining.constant.Constant.Index;

public class ExcelUtil {

	public static List<String[]> read(String filename) throws IOException {
		InputStream inputStream = new FileInputStream(new File(filename));
		return read(filename, inputStream, -1);
	}

	public static List<String[]> read(String filename, InputStream inputStream, int startRow) throws IOException {
		if (inputStream == null) {
			throw new IllegalArgumentException("inputStream is null");
		}
		return read(filename, inputStream, startRow, -1, null);
	}

	public static List<String[]> read(String filename, InputStream inputStream, int startRow, int rowNum)
			throws IOException {
		if (inputStream == null) {
			throw new IllegalArgumentException("inputStream is null");
		}
		return read(filename, inputStream, startRow, rowNum, null);
	}

	public static List<String[]> read(String filename, InputStream inputStream, int start, int rows, Integer... indexes)
			throws FileNotFoundException, IOException {

		List<String[]> list = new ArrayList<String[]>();
		Workbook workbook;
		if (filename.endsWith("xls")) {
			workbook = new HSSFWorkbook(inputStream);
		} else {
			workbook = new XSSFWorkbook(inputStream);
		}
		Sheet sheet = workbook.getSheetAt(0);
		int rowNum = sheet.getLastRowNum(); // 行数
		int colNum = sheet.getRow(0).getLastCellNum(); // 列数
		if (null == indexes || indexes.length == 0) {
			indexes = new Integer[colNum];
			for (int i = 0; i < colNum; i++) {
				indexes[i] = i;
			}
		}
		if (rows >= 0) {
			rowNum = rows > rowNum ? rowNum : rows;
		}
		start = start > rowNum ? rowNum : start;
		List<String> exitUrls = new ArrayList<String>();
		for (int i = start, x = 1; x <= rowNum; i++, x++) {
			String[] rowStr = new String[indexes.length];
			for (int j = 0; j < indexes.length; j++) {
				try {
					Cell cell = sheet.getRow(i).getCell(indexes[j]);
					if (cell.getCellType() == 0) {
						rowStr[j] = TimeUtil.convert(cell);
					} else {
						rowStr[j] = cell.toString();
					}
				} catch (Exception e) {
					rowStr[j] = "";
				}
				rowStr[j] = rowStr[j].replaceAll("\n", "");
				rowStr[j] = rowStr[j].replaceAll("\t", "");
				rowStr[j] = rowStr[j].replaceAll("\r", "");
				rowStr[j] = rowStr[j].replaceAll("\b", "");
				rowStr[j] = rowStr[j].replaceAll("\f", "");
			}
			if (CommonUtil.hasEmptyArray(rowStr)) {
				continue;
			}
			int exitIndex = exitUrls.indexOf(rowStr[Index.URL_INDEX]);
			if (exitIndex == -1) {
				list.add(rowStr);
				exitUrls.add(rowStr[Index.URL_INDEX]);
			}
		}
		inputStream.close();
		return list;
	}
	/**
	 * 允许有空表格的excel读取
	 * @param filename
	 * @param inputStream
	 * @param start 为负数代表从开始读取
	 * @param rows 为负数代表读取到最后一行
	 * @param indexes 要读取列下标，null代表读取全部列
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static List<String[]> readWithNull(String filename, InputStream inputStream, int start, int rows, Integer... indexes)
			throws FileNotFoundException, IOException {

		List<String[]> list = new ArrayList<String[]>();
		Workbook workbook;
		if (filename.endsWith("xls")) {
			workbook = new HSSFWorkbook(inputStream);
		} else {
			workbook = new XSSFWorkbook(inputStream);
		}
		Sheet sheet = workbook.getSheetAt(0);
		int rowNum = sheet.getLastRowNum(); // 行数
		int colNum = sheet.getRow(0).getLastCellNum(); // 列数
		if (null == indexes || indexes.length == 0) {
			indexes = new Integer[colNum];
			for (int i = 0; i < colNum; i++) {
				indexes[i] = i;
			}
		}
		if (rows >= 0) {
			rowNum = rows > rowNum ? rowNum : rows;
		}
		start = start > rowNum ? rowNum : start;
		List<String> exitUrls = new ArrayList<String>();
		for (int i = start, x = 1; x <= rowNum+1; i++, x++) {
			String[] rowStr = new String[indexes.length];
			for (int j = 0; j < indexes.length; j++) {
				try {
					Cell cell = sheet.getRow(i).getCell(indexes[j]);
					if (cell.getCellType() == 0) {
						rowStr[j] = TimeUtil.convert(cell);
					} else {
						rowStr[j] = cell.toString();
					}
				} catch (Exception e) {
					rowStr[j] = "";
				}
				rowStr[j] = rowStr[j].replaceAll("\n", "");
				rowStr[j] = rowStr[j].replaceAll("\t", "");
				rowStr[j] = rowStr[j].replaceAll("\r", "");
				rowStr[j] = rowStr[j].replaceAll("\b", "");
				rowStr[j] = rowStr[j].replaceAll("\f", "");
			}
			int exitIndex = exitUrls.indexOf(rowStr[Index.URL_INDEX]);
			if (exitIndex == -1) {
				list.add(rowStr);
				exitUrls.add(rowStr[Index.URL_INDEX]);
			}
		}
		inputStream.close();
		return list;
	}
	/**
	 * 允许有空行表格的excel读取
	 * @param filename
	 * @param inputStream
	 * @param start 为0代表从第一行开始读取
	 * @param rows 为负数代表读取到最后一行
	 * @param indexes 要读取列下标，null代表读取全部列
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static List<String[]> readWithNullRow(String filename, InputStream inputStream, int start, int rows, Integer... indexes)
			throws FileNotFoundException, IOException {

		List<String[]> list = new ArrayList<String[]>();
		Workbook workbook;
		if (filename.endsWith("xls")) {
			workbook = new HSSFWorkbook(inputStream);
		} else {
			workbook = new XSSFWorkbook(inputStream);
		}
		Sheet sheet = workbook.getSheetAt(0);
		int rowNum = sheet.getLastRowNum() + 1; // 行数
		int colNum = sheet.getRow(0).getLastCellNum(); // 列数
		if (null == indexes || indexes.length == 0) {
			indexes = new Integer[colNum];
			for (int i = 0; i < colNum; i++) {
				indexes[i] = i;
			}
		}
		if (rows >= 0) {
			rowNum = rows > rowNum ? rowNum : rows;
		}
		start = start > rowNum ? rowNum : start;
		List<String> exitUrls = new ArrayList<String>();
		for (int i = start, x = 1; x <= rowNum; i++, x++) {
			String[] rowStr = new String[indexes.length];
			for (int j = 0; j < indexes.length; j++) {
				try {
					Cell cell = sheet.getRow(i).getCell(indexes[j]);
					if (cell.getCellType() == 0) {
						rowStr[j] = TimeUtil.convert(cell);
					} else {
						rowStr[j] = cell.getStringCellValue();
					}
				} catch (Exception e) {
					rowStr[j] = "";
				}
				rowStr[j] = rowStr[j].replaceAll("\n", "");
				rowStr[j] = rowStr[j].replaceAll("\t", "");
				rowStr[j] = rowStr[j].replaceAll("\r", "");
				rowStr[j] = rowStr[j].replaceAll("\b", "");
				rowStr[j] = rowStr[j].replaceAll("\f", "");
			}
			list.add(rowStr);
//			int exitIndex = exitUrls.indexOf(rowStr[Index.URL_INDEX]);
//			if (exitIndex == -1) {
				
//				exitUrls.add(rowStr[Index.URL_INDEX]);
//			}
		}
		inputStream.close();
		return list;
	}
	
	public static HSSFWorkbook exportToExcel(@SuppressWarnings("unchecked") List<String[]>... lists) {
		HSSFWorkbook workbook = new HSSFWorkbook();
		for (int k = 0; k < lists.length; k++) {
			List<String[]> list = lists[k];
			Sheet sheet = workbook.createSheet("sheet" + (k + 1));
			for (int i = 0; i < list.size(); i++) {
				String[] rowList = list.get(i);
				Row row = sheet.createRow(i);
				for (int j = 0; j < rowList.length; j++) {
					Cell cell = row.createCell(j);
					cell.setCellValue(rowList[j]);
				}
			}
		}
		return workbook;
	}

	// ----------------------------------新增工具-------------------------------------------
	// 读取停用词文件（excel），只允许一列
	public static List<String> read(String filename, InputStream inputStream)
			throws FileNotFoundException, IOException {
		if (inputStream == null) {
			throw new IllegalArgumentException("inputStream is null");
		}
		List<String> list = new ArrayList<String>();
		Workbook workbook;
		if (filename.endsWith("xls")) {
			workbook = new HSSFWorkbook(inputStream);
		} else {
			workbook = new XSSFWorkbook(inputStream);
		}
		Sheet sheet = workbook.getSheetAt(0);
		int rowNum = sheet.getLastRowNum();
		int colNum = sheet.getRow(0).getLastCellNum();
		if (colNum > 1) {
			inputStream.close();
			return null;
		}
		for (int i = 0; i <= rowNum; i++) {
			String word = new String();
			Cell cell = sheet.getRow(i).getCell(0);
			if (Cell.CELL_TYPE_NUMERIC == cell.getCellType()) {
				word = cell.toString();
				String[] words = word.split("\\.");
				if (words.length > 1 && Integer.parseInt(words[1]) == 0) {
					word = words[0];
				}
			} else {
				word = cell.toString();
			}
			if ("" == word || null == word) {
				continue;
			}
			list.add(word);
		}
		inputStream.close();
		return list;
	}

	// ----------------------------------新增工具-------------------------------------------
	// 读取并去重返回所有属性的excel文件
	public static List<String[]> readAll(String filename, InputStream inputStream, int urlIndex)
			throws FileNotFoundException, IOException {

		List<String[]> list = new ArrayList<String[]>();
		Workbook workbook;
		if (filename.endsWith("xls")) {
			workbook = new HSSFWorkbook(inputStream);
		} else {
			workbook = new XSSFWorkbook(inputStream);
		}
		Sheet sheet = workbook.getSheetAt(0);
		int rowNum = sheet.getLastRowNum();
		int colNum = sheet.getRow(0).getLastCellNum();
		List<String> exitUrls = new ArrayList<String>();
		for (int i = 0; i <= rowNum; i++) {
			String[] rowStr = new String[colNum];
			for (int j = 0; j < colNum; j++) {
				try {
					Cell cell = sheet.getRow(i).getCell(j);

					if (cell.getCellType() == 0) {
						rowStr[j] = TimeUtil.convert(cell);
					} else {
						rowStr[j] = cell.toString();
					}
				} catch (Exception e) {
					rowStr[j] = "";
				}
				rowStr[j] = rowStr[j].replaceAll("\n", "");
				rowStr[j] = rowStr[j].replaceAll("\t", "");
				rowStr[j] = rowStr[j].replaceAll("\r", "");
				rowStr[j] = rowStr[j].replaceAll("\b", "");
				rowStr[j] = rowStr[j].replaceAll("\f", "");
			}
			if (CommonUtil.isEmptyArray(rowStr)) {
				continue;
			}
			int exitIndex = exitUrls.indexOf(rowStr[urlIndex]);
			if (exitIndex == -1) {
				exitUrls.add(rowStr[urlIndex]);
				list.add(rowStr);
			}
		}
		inputStream.close();
		return list;
	}
	
	/**
	 * 读取文件所有的内容，不去重。去掉空行
	 */
	public static List<String[]> readAll(String filename)
			throws FileNotFoundException, IOException {
		List<String[]> list = new ArrayList<String[]>();
		Workbook workbook;
		InputStream inputStream = new FileInputStream(new File(filename));
		if (filename.endsWith("xls")) {
			workbook = new HSSFWorkbook(inputStream);
		} else {
			workbook = new XSSFWorkbook(inputStream);
		}
		Sheet sheet = workbook.getSheetAt(0);
		int rowNum = sheet.getLastRowNum(); // 行数
		int colNum = sheet.getRow(0).getLastCellNum(); // 列数
		for (int i = 0; i <= rowNum; i++) {
			String[] rowStr = new String[colNum];
			for (int j = 0; j < colNum; j++) {
				try {
					Cell cell = sheet.getRow(i).getCell(j);
					if (cell.getCellType() == 0) {
						rowStr[j] = TimeUtil.convert(cell);
					} else {
						rowStr[j] = cell.toString();
					}
				} catch (Exception e) {
					rowStr[j] = "";
				}
				rowStr[j] = rowStr[j].replaceAll("\n", "");
				rowStr[j] = rowStr[j].replaceAll("\t", "");
				rowStr[j] = rowStr[j].replaceAll("\r", "");
				rowStr[j] = rowStr[j].replaceAll("\b", "");
				rowStr[j] = rowStr[j].replaceAll("\f", "");
			}
			if (CommonUtil.isEmptyArray(rowStr)) {
				continue;
			}
			list.add(rowStr);
		}
		inputStream.close();
		return list;
	}

	/**
	 * 将带标记的信息导出到Excel文件中
	 * 带标记的行字体样式为红色
	 * @param cluster	要导出的内容
	 * @param marked	待标记的id集合（每个类中的下标） 
	 * @return
	 */
	public static HSSFWorkbook exportToExcelMarked(List<String[]> cluster, List<Integer> marked) {
		HSSFWorkbook workbook = new HSSFWorkbook();
		//生成单元格样式
        HSSFCellStyle markedrowStyle = workbook.createCellStyle();
        //新建font实体
        HSSFFont hssfFont = workbook.createFont();
        //设置字体颜色 ----红色标红
        hssfFont.setColor(HSSFColor.RED.index);
		markedrowStyle.setFont(hssfFont);
		
		Sheet sheet = workbook.createSheet("泛数据" );
		String[] rowList = cluster.get(0);
		for(String t : rowList){
			System.out.print(t+"\t");
		}
		Row row = sheet.createRow(0);
		for (int j = 0; j < rowList.length; j++) {
			Cell cell = row.createCell(j);
			cell.setCellValue(rowList[j]);
		}
		
//		cluster.remove(0);
		int index = 0 ;
		int count = 0;
		for (int i = 0; i < cluster.size(); i++) {
			
			rowList = cluster.get(i);
			if(i == 0){
				row = sheet.createRow(i);
				for (int j = 0; j < rowList.length; j++) {
					Cell cell = row.createCell(j);
					cell.setCellValue(rowList[j]);
				}
				continue;
			}
			if(CommonUtil.isEmptyArray(rowList)){				
				index = 0;
				count++;
				continue;
			}
			//从2开始，否则第一行表头会被覆盖
			row = sheet.createRow(i);
			if(index == marked.get(count)){
				for (int j = 0; j < rowList.length; j++) {
					Cell cell = row.createCell(j);
					cell.setCellValue(rowList[j]);
					cell.setCellStyle(markedrowStyle);
				}
			}else{
				for (int j = 0; j < rowList.length; j++) {
					Cell cell = row.createCell(j);
					cell.setCellValue(rowList[j]);
				}
			}
			
			++index;
		}
		
		return workbook;
	}

}
