package app.batch.config;

import app.batch.domain.Customer;
import app.batch.listener.JobCompletionListener;
import app.batch.repository.CustomerRepository;
import app.batch.step.Processor;
import app.batch.step.Writer;
import lombok.AllArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.domain.Sort;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableBatchProcessing
@AllArgsConstructor
public class BatchConfig {
    private DataSource dataSource;
    private JobBuilderFactory jobBuilderFactory;
    private StepBuilderFactory stepBuilderFactory;
    private CustomerRepository customerRepository;
    private Writer writer;

    @Bean
    public Job processJob(){
        return jobBuilderFactory.get("processJob")
                .incrementer(new RunIdIncrementer()).listener(listener())
                .flow(orderStep1()).end().build();
    }
    @Bean
    public Step orderStep1() {
        return stepBuilderFactory.get("orderStep1").<Customer, Customer>chunk(10)
                .reader(reader())
                .processor(new Processor())
                .writer(writer)
                .taskExecutor(taskExecutor())
                .build();
    }

    @Bean
    public RepositoryItemReader<Customer> reader() {
        final Map<String, Sort.Direction> sortKeys = new HashMap<>();
        sortKeys.put("id", Sort.Direction.ASC);
        RepositoryItemReader<Customer> writer = new RepositoryItemReader<>();
        writer.setRepository(customerRepository);
        writer.setMethodName("findAll");
        writer.setSort(sortKeys);
        return writer;
    }

    @Bean
    public JobExecutionListener listener() {
        return new JobCompletionListener();
    }

    @Bean
    public TaskExecutor taskExecutor() {
        SimpleAsyncTaskExecutor asyncTaskExecutor = new SimpleAsyncTaskExecutor();
        asyncTaskExecutor.setConcurrencyLimit(10);
        return asyncTaskExecutor;
    }
}
