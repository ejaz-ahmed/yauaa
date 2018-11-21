package nl.basjes.parse.useragent.utils;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TestPrefixLookup {

    private static final Logger LOG = LoggerFactory.getLogger(TestPrefixLookup.class);

    @Test
    public void testLookup(){
        Map<String, String> prefixMap = new HashMap<>();
        prefixMap.put("1",    "Result 1");
        prefixMap.put("12",   "Result 12");
        prefixMap.put("123",  "Result 123");
        // The 1234 is missing !!!
        prefixMap.put("12345",  "Result 12345");

        PrefixLookup prefixLookup = new PrefixLookup(prefixMap);

        assertNull(prefixLookup.findLongestMatchingPrefix("MisMatch"));

        assertEquals("Wrong result for '1'",             "Result 1",        prefixLookup.findLongestMatchingPrefix("1"));
        assertEquals("Wrong result for '12'",            "Result 12",       prefixLookup.findLongestMatchingPrefix("12"));
        assertEquals("Wrong result for '123'",           "Result 123",      prefixLookup.findLongestMatchingPrefix("123"));
        assertEquals("Wrong result for '1234'",          "Result 123",      prefixLookup.findLongestMatchingPrefix("1234"));
        assertEquals("Wrong result for '12345'",         "Result 12345",    prefixLookup.findLongestMatchingPrefix("12345"));

        assertEquals("Wrong result for '1234'",          "Result 123",      prefixLookup.findLongestMatchingPrefix("1234"));
        assertEquals("Wrong result for '12 Something'",  "Result 12",       prefixLookup.findLongestMatchingPrefix("12 Something"));
        assertEquals("Wrong result for '1111'",          "Result 1",        prefixLookup.findLongestMatchingPrefix("1111"));
    }

    @Test
    public void testLookupSpeed(){
        Map<String, String> prefixMap = new HashMap<>();
        prefixMap.put("1",    "Result 1");
        for (int i = 10; i < 1000; i++) {
            prefixMap.put("" + i, "Something");
        }
        PrefixLookup prefixLookup = new PrefixLookup(prefixMap);

        long iterations = 1000000;

        long start = System.nanoTime();
        for (int i = 0; i<iterations; i++) {
            prefixLookup.findLongestMatchingPrefix("999");
        }
        long stop = System.nanoTime();
        LOG.info("Speed stats: {} runs took {}ms --> {}us each.", iterations, (stop - start)/1000000, ((stop - start)/iterations)/1000);
        assertEquals("Result 1", prefixLookup.findLongestMatchingPrefix("1"));
    }

}
