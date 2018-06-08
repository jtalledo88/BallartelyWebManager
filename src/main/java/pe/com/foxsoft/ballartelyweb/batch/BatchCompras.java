package pe.com.foxsoft.ballartelyweb.batch;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.RowMapper;

import pe.com.foxsoft.ballartelyweb.batch.processor.ComprasItemProcessor;

@Configuration
@EnableBatchProcessing
public class BatchCompras {
	
	private static final String SQL_READER = "select content from notes_app.notes";

	private static final String SQL_WRITTER = "insert into notes_app.notes_long(notes_longcol) values(?)";

	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	@Autowired
	private DataSource dataSource;
	
	@Bean
	public JdbcCursorItemReader<String> reader(){
		JdbcCursorItemReader<String> reader = new JdbcCursorItemReader<>();
		reader.setDataSource(dataSource);
		reader.setSql(SQL_READER);
		reader.setRowMapper(new NotesRowMapper());
		
		return reader;
	}
	
	@Bean
	public ComprasItemProcessor processor() {
		return new ComprasItemProcessor();
	}
	
	@Bean
	public JdbcBatchItemWriter<Integer> writter() {
		JdbcBatchItemWriter<Integer> writter = new JdbcBatchItemWriter<>();
		writter.setDataSource(dataSource);
		writter.setSql(SQL_WRITTER);
		writter.setItemPreparedStatementSetter(new NotesLongPreparedStm());
		
		return writter;
	}
	
	@Bean
	public Step step1() {
		return stepBuilderFactory
				.get("step1")
				.<String, Integer> chunk(10)
				.reader(reader())
				.processor(processor())
				.writer(writter())
				.build();
	}
	
	@Bean
	public Job insertLongJob() {
		return jobBuilderFactory
				.get("insertLongJob")
				.incrementer(new RunIdIncrementer())
				.flow(step1())
				.end()
				.build();
	}

	public JobBuilderFactory getJobBuilderFactory() {
		return jobBuilderFactory;
	}

	public void setJobBuilderFactory(JobBuilderFactory jobBuilderFactory) {
		this.jobBuilderFactory = jobBuilderFactory;
	}

	public StepBuilderFactory getStepBuilderFactory() {
		return stepBuilderFactory;
	}

	public void setStepBuilderFactory(StepBuilderFactory stepBuilderFactory) {
		this.stepBuilderFactory = stepBuilderFactory;
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	private class NotesRowMapper implements RowMapper<String> {

		@Override
		public String mapRow(ResultSet rs, int rowNum) throws SQLException {
			return rs.getString(1);
		}
		
	}
	private class NotesLongPreparedStm implements ItemPreparedStatementSetter<Integer> {

		@Override
		public void setValues(Integer out, PreparedStatement ps) throws SQLException {
			ps.setInt(1, out);
			
		}
		
	}

}
