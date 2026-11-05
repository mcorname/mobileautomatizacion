package com.nttdata.runners;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        plugin = {
                "pretty",
                "html:target/cucumber-report.html",
                "json:target/cucumber-report.json"
        },
        features = "src/test/resources/features",
        glue = {
                "com.nttdata.stepsdefinitions",
                "com.nttdata.hooks"
        },
        tags = "@CarritoCompras",
        monochrome = true
)
public class CucumberTestSuite {
}
