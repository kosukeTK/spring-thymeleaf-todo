/**
 * 
 */
package com.kosuke.utils;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.kosuke.todo.Task;

/**
 * CSV出力用クラス
 * @author torit
 *
 */
public class OutputCSV {

	
	/**
	 * CSVを書き込み
	 * @param task
	 * @return
	 * @throws JsonProcessingException
	 */
	public static Object write(List<?> csvData) throws JsonProcessingException {
		CsvMapper mapper = new CsvMapper();
		mapper.registerModules(new JavaTimeModule());
		CsvSchema schema = mapper.schemaFor(Task.class).withHeader();
		return mapper.writer(schema).writeValueAsString(csvData);
	}
	
}
