package app.batch.step;

import app.batch.domain.Customer;
import org.springframework.batch.item.ItemProcessor;

public class Processor implements ItemProcessor<Customer, Customer> {

    @Override
    public Customer process(Customer data) throws Exception {
        if (data.getCountry().equals("Argentina")){
            return data;
        }
        return null;
    }
}
