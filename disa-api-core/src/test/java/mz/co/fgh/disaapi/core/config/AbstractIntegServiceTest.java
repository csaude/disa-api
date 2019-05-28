/**
 *
 */
package mz.co.fgh.disaapi.core.config;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import mz.org.fgh.disaapi.core.DisaApiCoreApplication;

/**
 * @author Stélio Moiane
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { DisaApiCoreApplication.class })
@ActiveProfiles("test")
public abstract class AbstractIntegServiceTest extends AbstractServiceTest {
}
