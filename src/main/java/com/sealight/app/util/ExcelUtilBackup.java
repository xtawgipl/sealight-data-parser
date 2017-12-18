package com.sealight.app.util;

import com.sealight.app.bean.Constants;
import com.sealight.app.bean.DataBean;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * excel表格工具类
 *
 * @author zhangjj
 * @create 2017-12-11 21:25
 **/
public class ExcelUtilBackup {

    public static void excelExport(String sheelName, List<String> titles, List<DataBean> dataSet) {

        /** 表头占的行数*/
        int headRow = 2;

        // 创建excel
        HSSFWorkbook wb = new HSSFWorkbook();

        // 创建sheet
        HSSFSheet sheet = wb.createSheet(sheelName);
        sheet.setDefaultColumnWidth(30);
        sheet.setDefaultRowHeight((short) 500);

        // 创建一行
        HSSFRow rowTitle = sheet.createRow(0);

        // 创建标题栏样式
        HSSFCellStyle styleTitle = wb.createCellStyle();
        styleTitle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 居中
        HSSFFont fontTitle = wb.createFont();
        // 宋体加粗
        fontTitle.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        fontTitle.setFontName("宋体");
        fontTitle.setFontHeight((short) 200);
        styleTitle.setFont(fontTitle);


        HSSFCell cellTitle = null;
        cellTitle = rowTitle.createCell(2);
        cellTitle.setCellValue("前灯");
        cellTitle.setCellStyle(styleTitle);

        cellTitle = rowTitle.createCell(6);
        cellTitle.setCellValue("外灯");
        cellTitle.setCellStyle(styleTitle);

        cellTitle = rowTitle.createCell(18);
        cellTitle.setCellValue("内灯");
        cellTitle.setCellStyle(styleTitle);

        //在sheet里增加合并单元格
        CellRangeAddress cra = new CellRangeAddress(0, 0, 2, 5);
        sheet.addMergedRegion(cra);
        cra = new CellRangeAddress(0, 0, 6, 17);
        sheet.addMergedRegion(cra);
        cra = new CellRangeAddress(0, 0, 18, 25);
        sheet.addMergedRegion(cra);


        rowTitle = sheet.createRow(1);
        // 在行上创建1列
        for(int i = 0; i < titles.size(); ++i){
            // 列标题及样式
            cellTitle = rowTitle.createCell(i);
            cellTitle.setCellValue(titles.get(i));
            cellTitle.setCellStyle(styleTitle);
        }


        HSSFCellStyle styleCenter = wb.createCellStyle();
        styleCenter.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 居中

        int rowStart = headRow;//合并单元格的行起始index
        // 取数据
        for (int i = 0; i < dataSet.size(); i++) {
            DataBean item = dataSet.get(i);
            HSSFRow row = sheet.createRow(i + headRow);

            /** 生产年份*/
            HSSFCell cell = row.createCell(0);
            if(i != 0 && item.getYear().equals(dataSet.get(i - 1).getYear())){//同一年的不要写年份数据
                cell.setCellValue("");
            }else{
                if(rowStart < i + headRow){
                    /*
                     * 设定合并单元格区域范围
                     *  firstRow  0-based
                     *  lastRow   0-based
                     *  firstCol  0-based
                     *  lastCol   0-based
                     */
                    cra = new CellRangeAddress(rowStart, i + headRow - 1, 0, 0);
                    //在sheet里增加合并单元格
                    sheet.addMergedRegion(cra);
                    rowStart = i + headRow;
                }
                cell.setCellValue(item.getYear());
            }
            cell.setCellStyle(styleCenter);


            /** 汽车型号*/
            cell = row.createCell(1);
            cell.setCellValue(item.getType());
            cell.setCellStyle(styleCenter);

            /** 灯 */
            for(int j = 0; j < Constants.alLightList.size(); ++j){
                cell = row.createCell(2 + j);
                cell.setCellValue(item.getLightMap().get(Constants.alLightList.get(j)));
                cell.setCellStyle(styleCenter);
            }
        }

        FileOutputStream fout = null;
        try {
            fout = new FileOutputStream(Constants.FILE_PATH + Constants.FILE_SIGN + sheelName+".xls");
            wb.write(fout);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(fout != null){
                try {
                    fout.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(wb != null){
                try {
                    wb.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


        System.out.println(sheelName + " 表生成完成");
    }

    public static void main(String[] args) {
        ExcelUtilBackup.excelExport("丰田", Constants.titles, new ArrayList<DataBean>());
    }

}
