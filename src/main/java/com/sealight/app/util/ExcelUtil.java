package com.sealight.app.util;

import com.sealight.app.bean.LightTypeListData;
import com.sealight.app.bean.Constants;
import com.sealight.app.bean.ParamBean;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * excel表格工具类
 *
 * @author zhangjj
 * @create 2017-12-11 21:25
 **/
public class ExcelUtil {

    public static void excelExport(Map<Integer, String> yearMap, String sheelName, LightTypeListData lightTypeListData, List<ParamBean> paramList) {

        /** 表头占的行数*/
        int headRow = 2;
        /** 前两行 */
        int startCol = 2;

        // 创建excel
        XSSFWorkbook wb = new XSSFWorkbook();

        // 创建sheet
        XSSFSheet sheet = wb.createSheet(sheelName);
        sheet.setDefaultColumnWidth(30);
        sheet.setDefaultRowHeight((short) 600);

        // 创建一行
        XSSFRow rowTitle = sheet.createRow(0);

        // 创建标题栏样式
        XSSFCellStyle styleTitle = wb.createCellStyle();
        styleTitle.setAlignment(HSSFCellStyle.ALIGN_CENTER);//水平居中
        styleTitle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直居中
        styleTitle.setFillBackgroundColor(HSSFColor.GREY_25_PERCENT.index);
        styleTitle.setFillPattern(HSSFCellStyle.ALIGN_CENTER);
        styleTitle.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
        styleTitle.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
        styleTitle.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
        styleTitle.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框

        XSSFFont fontTitle = wb.createFont();
        // 宋体加粗
        fontTitle.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        fontTitle.setFontName("宋体");
        fontTitle.setFontHeight((short) 200);
        styleTitle.setFont(fontTitle);
        styleTitle.setWrapText(true);


        CellRangeAddress cra = null;//在sheet里增加合并单元格
        XSSFCell cellTitle = null;

        cellTitle = rowTitle.createCell(0);
        cellTitle.setCellValue("型号");
        cellTitle.setCellStyle(styleTitle);
        cra = new CellRangeAddress(0, 1, 0, 0);
        sheet.addMergedRegion(cra);

        cellTitle = rowTitle.createCell(1);
        cellTitle.setCellValue("年份");
        cellTitle.setCellStyle(styleTitle);
        cra = new CellRangeAddress(0, 1, 1, 1);
        sheet.addMergedRegion(cra);


        Set<String> forwardLightList = lightTypeListData.getForwardLightList();
        if(forwardLightList != null && !forwardLightList.isEmpty()){
            cellTitle = rowTitle.createCell(startCol);
            cellTitle.setCellValue("前灯");
            cellTitle.setCellStyle(styleTitle);

            cra = new CellRangeAddress(0, 0, startCol, forwardLightList.size() + startCol - 1);
            sheet.addMergedRegion(cra);
        }

        Set<String> exteriorLightList = lightTypeListData.getExteriorLightList();
        if(exteriorLightList != null && !exteriorLightList.isEmpty()){
            cellTitle = rowTitle.createCell((forwardLightList == null ? 0 : forwardLightList.size()) + startCol);
            cellTitle.setCellValue("外灯");
            cellTitle.setCellStyle(styleTitle);

            cra = new CellRangeAddress(0, 0,
                    (forwardLightList == null ? 0 : forwardLightList.size()) + startCol,
                    (forwardLightList == null ? 0 : forwardLightList.size()) + exteriorLightList.size() + startCol - 1);
            sheet.addMergedRegion(cra);
        }

        Set<String> interiorLightList = lightTypeListData.getInteriorLightList();
        if(interiorLightList != null && !interiorLightList.isEmpty()){
            cellTitle = rowTitle.createCell((forwardLightList == null ? 0 : forwardLightList.size()) +
                    (exteriorLightList == null ? 0 : exteriorLightList.size()) + startCol);
            cellTitle.setCellValue("内灯");
            cellTitle.setCellStyle(styleTitle);

            cra = new CellRangeAddress(0, 0,
                    (forwardLightList == null ? 0 : forwardLightList.size()) + (exteriorLightList == null ? 0 : exteriorLightList.size()) + startCol,
                    (forwardLightList == null ? 0 : forwardLightList.size()) + (exteriorLightList == null ? 0 : exteriorLightList.size()) + interiorLightList.size() + startCol - 1);
            sheet.addMergedRegion(cra);
        }


        rowTitle = sheet.createRow(1);
        rowTitle.setHeight((short) 800);
        int k = 2;
        if(forwardLightList != null){
            for(String forward : forwardLightList){
                // 列标题及样式
                cellTitle = rowTitle.createCell(k++);
                cellTitle.setCellValue(forward);
                cellTitle.setCellStyle(styleTitle);
            }
        }
        if(exteriorLightList != null){
            for(String exterior : exteriorLightList){
                // 列标题及样式
                cellTitle = rowTitle.createCell(k++);
                cellTitle.setCellValue(exterior);
                cellTitle.setCellStyle(styleTitle);
            }
        }
        if(interiorLightList != null){
            for(String interior : interiorLightList){
                // 列标题及样式
                cellTitle = rowTitle.createCell(k++);
                cellTitle.setCellValue(interior);
                cellTitle.setCellStyle(styleTitle);
            }
        }



        XSSFCellStyle styleCenter = wb.createCellStyle();
        styleCenter.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 居中
        styleCenter.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直居中
        styleCenter.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
        styleCenter.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
        styleCenter.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
        styleCenter.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框


        XSSFCellStyle forwardCenter = wb.createCellStyle();
        forwardCenter.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 居中
        forwardCenter.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直居中
        forwardCenter.setFillBackgroundColor(HSSFColor.LIGHT_TURQUOISE.index);
        forwardCenter.setFillPattern(HSSFCellStyle.ALIGN_CENTER);
        forwardCenter.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
        forwardCenter.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
        forwardCenter.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
        forwardCenter.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框

        XSSFCellStyle exteriorCenter = wb.createCellStyle();
        exteriorCenter.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 居中
        exteriorCenter.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直居中
        exteriorCenter.setFillBackgroundColor(HSSFColor.LEMON_CHIFFON.index);
        exteriorCenter.setFillPattern(HSSFCellStyle.ALIGN_CENTER);
        exteriorCenter.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
        exteriorCenter.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
        exteriorCenter.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
        exteriorCenter.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框

        XSSFCellStyle interiorCenter = wb.createCellStyle();
        interiorCenter.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 居中
        interiorCenter.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直居中
        interiorCenter.setFillBackgroundColor(HSSFColor.CORNFLOWER_BLUE.index);
        interiorCenter.setFillPattern(HSSFCellStyle.ALIGN_CENTER);
        interiorCenter.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
        interiorCenter.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
        interiorCenter.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
        interiorCenter.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框

        int rowStart = headRow;//合并单元格的行起始index
        // 取数据
        for (int i = 0; i < paramList.size(); i++) {
            ParamBean item = paramList.get(i);
            XSSFRow row = sheet.createRow(i + headRow);

            /** 汽车型号*/
            XSSFCell cell = row.createCell(0);
            if(i != 0 && item.getType().equals(paramList.get(i - 1).getType())){//同一型号的不要写型号数据
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
                cell.setCellValue(item.getType());
            }
            cell.setCellStyle(styleCenter);


            /** 生产年份*/
            cell = row.createCell(1);
            cell.setCellValue(yearMap.get(item.getYearId()));
            cell.setCellStyle(styleCenter);

            /** 灯 */
            int j = startCol;
            if(forwardLightList != null){
                for(String forward : forwardLightList){
                    cell = row.createCell(j++);
                    if(item.getForwardLightMap() == null || item.getForwardLightMap().isEmpty()){
                        cell.setCellValue("");
                    }else{
                        cell.setCellValue(item.getForwardLightMap().get(forward));
                    }
                    cell.setCellStyle(forwardCenter);
                }
            }

            if(exteriorLightList != null){
                for(String exterior : exteriorLightList){
                    cell = row.createCell(j++);
                    if(item.getExteriorLightMap() == null || item.getExteriorLightMap().isEmpty()){
                        cell.setCellValue("");
                    }else{
                        cell.setCellValue(item.getExteriorLightMap().get(exterior));
                    }
                    cell.setCellStyle(exteriorCenter);
                }
            }

            if(interiorLightList != null){
                for(String interior : interiorLightList){
                    cell = row.createCell(j++);
                    if(item.getInteriorLightMap() == null || item.getInteriorLightMap().isEmpty()){
                        cell.setCellValue("");
                    }else{
                        cell.setCellValue(item.getInteriorLightMap().get(interior));
                    }
                    cell.setCellStyle(interiorCenter);
                }
            }
        }

        FileOutputStream fout = null;
        try {
            fout = new FileOutputStream(Constants.XLS_FILE_PATH + Constants.FILE_SIGN + sheelName+".xlsx");
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
        }


        System.out.println(sheelName + " 表生成完成");
    }


}
