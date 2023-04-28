package mz.org.fgh.disaapi.integ.test.integration;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Base64;
import java.util.HashMap;

import javax.inject.Inject;
import javax.persistence.EntityManagerFactory;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.json.JSONException;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import br.com.six2six.fixturefactory.processor.HibernateProcessor;
import mz.co.fgh.disaapi.core.fixturefactory.ImplementingPartnerTemplate;
import mz.co.fgh.disaapi.core.fixturefactory.ViralLoadTemplate;
import mz.co.msaude.boot.frameworks.exception.BusinessException;
import mz.org.fgh.disaapi.core.ip.ImplementingPartner;
import mz.org.fgh.disaapi.core.orgunit.model.OrgUnit;
import mz.org.fgh.disaapi.core.result.model.HIVVLLabResult;
import mz.org.fgh.disaapi.core.result.model.LabResult;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ContextConfiguration
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
public class ViralLoadResourceTest {

    private static final String NOT_PROCESSED_URL = "/viralloads/requestProvince/not-processed?notProcessedNids={notProcessedNids}&reasonForNotProcessing={reasonForNotProcessing}&defaultLocationUuid={defaultLocationUuid}";

    @Inject
    protected TestRestTemplate restTemplate;

    @Inject
    private EntityManagerFactory emFactory;

    private LabResult vl;

    protected ImplementingPartner fgh;

    @BeforeClass
    public static void setUp() {
        FixtureFactoryLoader.loadTemplates("mz.co.fgh.disaapi.core.fixturefactory");
    }

    @Before
    public void before() throws BusinessException {

        SessionFactory sessionFactory = emFactory.unwrap(SessionFactory.class);
        try (Session session = sessionFactory.openSession()) {

            Transaction tx = session.beginTransaction();

            HibernateProcessor hibernateProcessor = new HibernateProcessor(session);

            vl = Fixture.from(HIVVLLabResult.class)
                    .uses(hibernateProcessor)
                    .gimme(ViralLoadTemplate.VALID);

            fgh = Fixture.from(ImplementingPartner.class)
                    .uses(hibernateProcessor)
                    .gimme(ImplementingPartnerTemplate.FGH);

            for (OrgUnit orgUnit : fgh.getOrgUnits()) {
                orgUnit.setImplementingPartnerId(fgh.getId());
                session.save(orgUnit);
            }

            tx.commit();
        }
    }

    @Test
    public void updateViralLoadNotProcessedShouldInactivateDuplicateRequestId() throws JSONException {
        HttpHeaders headers = createHttpContentTypeAndAuthorizationHeaders();
        HttpEntity<String> httpEntity = new HttpEntity<>("", headers);
        HashMap<String, String> urlVariables = new HashMap<String, String>();
        urlVariables.put("notProcessedNids", vl.getRequestId());
        urlVariables.put("reasonForNotProcessing", "duplicatedReqId");
        urlVariables.put("defaultLocationUuid", "3fa9eb13-8caf-4f69-92f3-e28e36cc2b55");

        ResponseEntity<String> response = restTemplate.exchange(NOT_PROCESSED_URL, HttpMethod.PUT, httpEntity,
                String.class, urlVariables);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        JSONAssert.assertEquals("[{entityStatus: 'INACTIVE'}]", response.getBody(), false);

    }

    private HttpHeaders createHttpContentTypeAndAuthorizationHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.add(HttpHeaders.AUTHORIZATION, "Basic " + performBasicAuthEncoding("fgh", "fgh"));
        return httpHeaders;
    }

    private String performBasicAuthEncoding(String user, String password) {
        String combined = user + ":" + password;
        byte[] encodedBytes = Base64.getEncoder().encode(combined.getBytes());
        return new String(encodedBytes);
    }

}
