package edu.coursemgr.excel;

import edu.coursemgr.utils.CommonUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenxianzhang on 2018/8/23 0023 下午 11:22
 */
public class ExcelReader {

    public int totalRows; //sheet中总行数
    public static int totalCells; //每一行总单元格数
    /**
     * read the Excel .xlsx,.xls
     * @param file jsp中的上传文件
     * @return
     * @throws IOException
     */
    public List<ArrayList<String>> readExcel(MultipartFile file) throws IOException {
        if(file==null||ExcelUtil.EMPTY.equals(file.getOriginalFilename().trim())){
            return null;
        }else{
            String postfix = ExcelUtil.getPostfix(file.getOriginalFilename());
            if(!ExcelUtil.EMPTY.equals(postfix)){
                if(ExcelUtil.OFFICE_EXCEL_2003_POSTFIX.equals(postfix)){
                    return readXls(file);
                }else if(ExcelUtil.OFFICE_EXCEL_2010_POSTFIX.equals(postfix)){
                    return readXlsx(file);
                }else{
                    return null;
                }
            }
        }
        return null;
    }
    /**
     * read the Excel 2010 .xlsx
     * @param file
     * @return
     * @throws IOException
     */
    @SuppressWarnings("deprecation")
    public List<ArrayList<String>> readXlsx(MultipartFile file){
        List<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
        // IO流读取文件
        InputStream input = null;
        XSSFWorkbook wb = null;
        ArrayList<String> rowList = null;
        try {
            input = file.getInputStream();
            // 创建文档
            wb = new XSSFWorkbook(input);
            //读取sheet(页)
            for(int numSheet=0;numSheet<wb.getNumberOfSheets();numSheet++){
                XSSFSheet xssfSheet = wb.getSheetAt(numSheet);
                if(xssfSheet == null){
                    continue;
                }
                totalRows = xssfSheet.getLastRowNum();
                //读取Row,从第一行开始
                for(int rowNum = 0;rowNum <= totalRows;rowNum++){
                    XSSFRow xssfRow = xssfSheet.getRow(rowNum);
                    if(xssfRow!=null){
                        rowList = new ArrayList<String>();
                        totalCells = xssfRow.getLastCellNum();
                        //读取列，从第一列开始
                        for(int c=0;c<=totalCells+1;c++){
                            XSSFCell cell = xssfRow.getCell(c);
                            if(cell==null){
                                rowList.add(ExcelUtil.EMPTY);
                                continue;
                            }
                            rowList.add(ExcelUtil.getXValue(cell).trim());
                        }
                        list.add(rowList);
                    }
                }
            }
            return list;
        } catch (IOException e) {
            e.printStackTrace();
        } finally{
            try {
                input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;

    }
    /**
     * read the Excel 2003-2007 .xls
     * @param file
     * @return
     * @throws IOException
     */
    public List<ArrayList<String>> readXls(MultipartFile file){
        List<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
        // IO流读取文件
        InputStream input = null;
        HSSFWorkbook wb = null;
        ArrayList<String> rowList = null;
        try {
            input = file.getInputStream();
            // 创建文档
            wb = new HSSFWorkbook(input);
            //读取sheet(页)
            for(int numSheet=0;numSheet<wb.getNumberOfSheets();numSheet++){
                HSSFSheet hssfSheet = wb.getSheetAt(numSheet);
                if(hssfSheet == null){
                    continue;
                }
                totalRows = hssfSheet.getLastRowNum();
                //读取Row,从第一行开始
                for(int rowNum = 0;rowNum <= totalRows;rowNum++){
                    HSSFRow hssfRow = hssfSheet.getRow(rowNum);
                    if(hssfRow!=null){
                        rowList = new ArrayList<String>();
                        totalCells = hssfRow.getLastCellNum();
                        //读取列，从第一列开始
                        for(short c=0;c<=totalCells+1;c++){
                            HSSFCell cell = hssfRow.getCell(c);
                            if(cell==null){
                                rowList.add(ExcelUtil.EMPTY);
                                continue;
                            }
                            rowList.add(ExcelUtil.getHValue(cell).trim());
                        }
                        list.add(rowList);
                    }
                }
            }
            return list;
        } catch (IOException e) {
            e.printStackTrace();
        } finally{
            try {
                input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    public void export(String sheetName, String fileName, List<String> columnList,
                       List<ArrayList<String>> dataList, String titleName,
                       HttpServletResponse response) throws Exception {
        XSSFWorkbook wb = new XSSFWorkbook();

        XSSFSheet sheet = wb.createSheet(sheetName);

        // 创建第0行 也就是标题
        XSSFRow row1 = sheet.createRow((int) 0);
        row1.setHeightInPoints(50);// 设备标题的高度
        // 第三步创建标题的单元格样式style2以及字体样式headerFont1
        XSSFCellStyle style2 = wb.createCellStyle();
        style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        style2.setFillForegroundColor(HSSFColor.LIGHT_TURQUOISE.index);
        style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        XSSFFont headerFont1 =  wb.createFont(); // 创建字体样式
        headerFont1.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD); // 字体加粗
        headerFont1.setFontName("黑体"); // 设置字体类型
        headerFont1.setFontHeightInPoints((short) 15); // 设置字体大小
        style2.setFont(headerFont1); // 为标题样式设置字体样式

        XSSFCell cell1 = row1.createCell(0);// 创建标题第一列
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0,
                columnList.size() - 1)); // 合并列标题
        sheet.autoSizeColumn(1, true);
        sheet.setDefaultColumnWidth(15);
        cell1.setCellValue(titleName); // 设置值标题
        cell1.setCellStyle(style2); // 设置标题样式

        // 创建第1行 也就是表头
        XSSFRow row = sheet.createRow((int) 1);
        row.setHeightInPoints(37);// 设置表头高度

        // 第四步，创建表头单元格样式 以及表头的字体样式
        XSSFCellStyle style = wb.createCellStyle();
//        style.setWrapText(true);// 设置自动换行
        style.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        style.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER); // 创建一个居中格式

        style.setBottomBorderColor(HSSFColor.BLACK.index);
        style.setBorderBottom(XSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(XSSFCellStyle.BORDER_THIN);
        style.setBorderRight(XSSFCellStyle.BORDER_THIN);
        style.setBorderTop(XSSFCellStyle.BORDER_THIN);

        XSSFFont headerFont = wb.createFont(); // 创建字体样式
        headerFont.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD); // 字体加粗
        headerFont.setFontName("黑体"); // 设置字体类型
        headerFont.setFontHeightInPoints((short) 10); // 设置字体大小
        style.setFont(headerFont); // 为标题样式设置字体样式

        // 第四.一步，创建表头的列
        for (int i = 0; i < columnList.size(); i++)
        {
            XSSFCell cell = row.createCell(i);
            cell.setCellValue(columnList.get(i));
            cell.setCellStyle(style);
        }

//        HSSFDataFormat df = wb.createDataFormat();
//        HSSFCellStyle contextStyle = wb.createCellStyle();
//        contextStyle.setDataFormat(df.getBuiltinFormat("#,##0.00"));

        // 第五步，创建单元格，并设置值
        for (int i = 0; i < dataList.size(); i++)
        {
            row = sheet.createRow((int) i + 2);
            // 为数据内容设置特点新单元格样式1 自动换行 上下居中
            XSSFCellStyle zidonghuanhang = wb.createCellStyle();
            zidonghuanhang.setWrapText(true);// 设置自动换行
            zidonghuanhang.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER); // 创建一个居中格式

            // 设置边框
            zidonghuanhang.setBottomBorderColor(HSSFColor.BLACK.index);
            zidonghuanhang.setBorderBottom(XSSFCellStyle.BORDER_THIN);
            zidonghuanhang.setBorderLeft(XSSFCellStyle.BORDER_THIN);
            zidonghuanhang.setBorderRight(XSSFCellStyle.BORDER_THIN);
            zidonghuanhang.setBorderTop(XSSFCellStyle.BORDER_THIN);

            // 为数据内容设置特点新单元格样式2 自动换行 上下居中左右也居中
            XSSFCellStyle zidonghuanhang2 = wb.createCellStyle();
            zidonghuanhang2.setWrapText(true);// 设置自动换行
            zidonghuanhang2
                    .setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER); // 创建一个上下居中格式
            zidonghuanhang2.setAlignment(XSSFCellStyle.ALIGN_CENTER);// 左右居中

            // 设置边框
            zidonghuanhang2.setBottomBorderColor(HSSFColor.BLACK.index);
            zidonghuanhang2.setBorderBottom(XSSFCellStyle.BORDER_THIN);
            zidonghuanhang2.setBorderLeft(XSSFCellStyle.BORDER_THIN);
            zidonghuanhang2.setBorderRight(XSSFCellStyle.BORDER_THIN);
            zidonghuanhang2.setBorderTop(XSSFCellStyle.BORDER_THIN);
            XSSFCell datacell = null;
            for (int j = 0; j < columnList.size(); j++)
            {
                datacell = row.createCell(j);
                String value = dataList.get(i).get(j);

                datacell.setCellStyle(zidonghuanhang2);
                if (CommonUtils.isNumber(value) && j > 2) {
//                    datacell.setCellStyle(contextStyle);
                    datacell.setCellValue(Double.valueOf(value));
                } else {
                    datacell.setCellValue(value);
                }
            }
        }

        // 第六步，将文件存到浏览器设置的下载位置
        String filename = fileName + ".xlsx";
//        response.setContentType("application/ms-excel;charset=UTF-8");
        response.setContentType("application/x-download");
        response.setHeader("Content-Disposition", "attachment;filename="
                .concat(String.valueOf(URLEncoder.encode(filename, "UTF-8"))));
        OutputStream out = response.getOutputStream();
        wb.write(out);// 将数据写出去
        out.flush();
        out.close();
    }
}
