package br.com.gm2;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import br.com.gm2.element.CrumbTest;

@RunWith(Suite.class)
@SuiteClasses({ MainTest.class, CrumbTest.class })
public class AllTests {

}
