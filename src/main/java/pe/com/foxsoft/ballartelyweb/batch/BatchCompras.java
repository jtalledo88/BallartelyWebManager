package pe.com.foxsoft.ballartelyweb.batch;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import pe.com.foxsoft.ballartelyweb.batch.processor.ComprasItemProcessor;
import pe.com.foxsoft.ballartelyweb.jpa.data.GuideHead;

@Configuration
@EnableBatchProcessing
public class BatchCompras {
	
	private static final String SQL_READER = "SELECT gh FROM GuideHead gh";

	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	@Autowired
	private DataSource dataSource;
	
	@Bean
	public ItemReader<GuideHead> reader(){
		JpaPagingItemReader<GuideHead> reader = new JpaPagingItemReader<>();
		reader.setEntityManagerFactory(entityManagerFactory().getObject());
		reader.setQueryString(SQL_READER);
		return reader;
	}
	
	@Bean
	public ComprasItemProcessor processor() {
		return new ComprasItemProcessor();
	}
	
	@Bean
	public ItemWriter<GuideHead> writter() {
		JpaItemWriter<GuideHead> writter = new JpaItemWriter<>();
		writter.setEntityManagerFactory(entityManagerFactory().getNativeEntityManagerFactory());
		return writter;
	}
	
	@Bean
	public Step step1() {
		return stepBuilderFactory
				.get("step1")
				.transactionManager(jpaTransactionManager())
				.<GuideHead, GuideHead> chunk(10)
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
	
	@Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean lef = new LocalContainerEntityManagerFactoryBean();
        lef.setDataSource(dataSource);
        lef.setPackagesToScan("pe.com.foxsoft.ballartelyweb.jpa.data");
        lef.setPersistenceUnitName("default");
        lef.setJpaVendorAdapter(jpaVendorAdapter());
        return lef;
	}

	
	public JpaVendorAdapter jpaVendorAdapter() {
		HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
		return jpaVendorAdapter;
	}
	
	
	public PlatformTransactionManager jpaTransactionManager() {
	       return new JpaTransactionManager(entityManagerFactory().getNativeEntityManagerFactory());
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

}
