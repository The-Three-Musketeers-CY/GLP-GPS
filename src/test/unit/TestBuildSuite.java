package test.unit;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({TestMapBuild.class, TestNetworkBuild.class, TestTransportBuild.class, TestWayTypeBuild.class})
public class TestBuildSuite {
}
