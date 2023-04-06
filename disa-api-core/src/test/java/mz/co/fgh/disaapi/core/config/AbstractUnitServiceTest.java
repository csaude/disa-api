/**
 *
 */
package mz.co.fgh.disaapi.core.config;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import mz.co.fgh.disaapi.core.DisaApiCoreUnitTestApplication;

/**
 * @author Stélio Moiane
 *
 */
@ActiveProfiles("unitTest")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { DisaApiCoreUnitTestApplication.class })
public abstract class AbstractUnitServiceTest {

}
