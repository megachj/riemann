package com.sunset.java.etc.excel;

import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class ExcelHandlerImpl implements ExcelHandler {
    private final String inputResource;
    private final String outputResource;

    @Override
    public void handle() throws Exception {
        XSSFWorkbook wb = new XSSFWorkbook(this.getClass().getClassLoader().getResourceAsStream(inputResource));
        List<String> idList = new ArrayList<>();
        for (Row row : wb.getSheetAt(0)) {
            if (row.getRowNum() < 1)
                continue;

            if (row.getCell(0) == null)
                break;

            idList.add(String.valueOf((int)row.getCell(0).getNumericCellValue()));
        }

        // file 없으면 에러남.
        URL output = this.getClass().getClassLoader().getResource(outputResource);
        FileOutputStream fo = new FileOutputStream(Paths.get(output.toURI()).toFile());

        String result = idList.stream().reduce((a, b) -> a.concat(", " + b)).orElse("");
        fo.write(result.getBytes());
    }
}
