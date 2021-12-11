package org.acme;

import io.quarkus.test.junit.QuarkusTest;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit5.CamelTestSupport;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

@QuarkusTest
public class TransactedTest extends CamelTestSupport {
    @Inject
    CamelContext context;

    @Override
    protected CamelContext createCamelContext() throws Exception {
        return this.context;
    }

    @Override
    protected RouteBuilder createRouteBuilder() {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("direct:in")
                        .transacted()
                        .to("mock:out");
            }
        };
    }

    @Test
    void test() throws InterruptedException {
        MockEndpoint mockEndpoint = getMockEndpoint("mock:out");
        mockEndpoint.expectedMessageCount(1);
        template.sendBody("direct:in", "hello");
        assertMockEndpointsSatisfied();
    }
}
