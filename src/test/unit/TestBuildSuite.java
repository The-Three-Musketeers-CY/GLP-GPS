package test.unit;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * This is the global test suite of unit tests.
 *
 * It includes two test cases : {@link TestMapBuild}, {@link TestNetworksBuild}, {@link TestTransportsBuild} and {@link TestWayTypesBuild}.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({TestMapBuild.class, TestNetworksBuild.class, TestTransportsBuild.class, TestWayTypesBuild.class})
public class TestBuildSuite {
}
