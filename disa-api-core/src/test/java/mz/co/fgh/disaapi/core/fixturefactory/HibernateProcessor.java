package mz.co.fgh.disaapi.core.fixturefactory;

import org.hibernate.Session;

import br.com.six2six.fixturefactory.processor.Processor;
import jakarta.persistence.Embeddable;

public class HibernateProcessor implements Processor {

    private Session session;

    public HibernateProcessor(Session session) {
        this.session = session;
    }

    @Override
    public void execute(Object result) {
        if (result.getClass().isAnnotationPresent(Embeddable.class))
            return;
        session.merge(result);
    }
}
