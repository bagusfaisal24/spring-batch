package app.batch.step;

import app.batch.domain.Customer;
import app.batch.domain.CustomerLog;
import app.batch.repository.CustomerLogRepository;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Writer implements ItemWriter<Customer> {

    private final CustomerLogRepository repository;

    @Autowired
    public Writer(CustomerLogRepository repository) {
        this.repository = repository;
    }

    @Override
    public void write(List<? extends Customer> customers) throws Exception {
        for (Customer customer : customers) {
            repository.save(init(customer));
        }
    }

    private CustomerLog init(Customer customer){
        CustomerLog log = new CustomerLog();
        log.setId(customer.getId());
        log.setFirstName(customer.getFirstName());
        log.setLastName(customer.getLastName());
        log.setDob(customer.getDob());
        log.setEmail(customer.getEmail());
        log.setGender(customer.getGender());
        log.setContactNo(customer.getContactNo());
        log.setCountry(customer.getCountry());
        return log;
    }
}
