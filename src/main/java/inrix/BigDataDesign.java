package inrix;

// You are given the following information, herein known as a record:
//   String vehicleId
//   double latitude
//   double longitude
//   int minutes - the number of minutes since the Unix epoch
// This will represent the location of a vehicle at a specified time

// You may represent and store the record(s) in any manner appropriate to solve the following problem

// If there are a trillion records, how would you build a system to answer the question:
//
// Given a location (latitude and longitude), a time range, and a distance
// How many vehicles were within the distance from the location during the time range?s
public class BigDataDesign
{

    /**
     * Defines the row keys used for storing data in hbase.
     */
    public static String primaryIndexType = "/vehicleId:int64/";

    public static String secondaryIndexType = "/latitude:double/longitude:double/timestamp:i64/";

    /**
     * The approach listed below uses the following.
     * <ol>
     *     <li>Thrift for IDL, thrift files for this model can be found in thrift package</li>
     *     <li>Hbase for storing data</li>
     *     <li>Apache Storm for processing</li>
     * </ol>
     */

    // The client application will create rows of form
    // car thrift payload structure defined in the thrift package.
    // /vehicleId:100 - car payload
    // /vehicledId:200 - car payload
    // /vehicledId:300 - car payload.

    /**
     * <p />
     * <ol>
     *     <li>My initial design is based on a real time processing model based on a system such as Apache Storm,
     *     where in the initial rows entering hbase will be rows of primaryIndexType as listed above.</li>
     *     <li>This will trigger downstream mapper and reducer which will be listening to keys of type primaryIndexType.</li>
     * </ol>
     */

    /**
     * Approach one to solve the problem using a mapper which listens to rows of primaryIndexType.
     *
     * The first level mapper processing will create rows of the form as listed below from the initial payload.
     * /latitude:100/longitude:200/timestamp:100000 - vehicleId 100 payload.
     * /latitude:100/longitude:200/timestamp:120000 - vehicleId 400 payload.
     * /latitude:100/longitude:300/timestamp:200000 - vehicleId 200 payload.
     * /latitude:100/longitude:300/timestamp:300000 - vehicleId 300 payload.
     * /latitude:100/longitude:200/timestamp:150000 - vehicleId 500 payload.
     *
     */

    /**
     * Second level processing, the following lists the stages of the reducer.
     * the reducer processing will look for keys of the secondaryIndexType.
     *
     * The reducer goes through the keys and groups the cars at a specific location using the Cars thrift object defined in the
     * thrift file.
     *
     * for instance this will be step by step output of reducer for the initial mapper above.
     *
     * /latitude:100/longitude:200/timestamp:100000 - cars{car{vehicleId:100}}
     * /latitude:100/longitude:200/timestamp:120000 - cars{car{vehicleId:100},car{vehicleId:400}}
     * /latitude:100/longitude:300/timestamp:130000 - cars{car{vehicleId:200}}
     * /latitude:100/longitude:300/timestamp:140000 - cars{car{vehicleId:200},car{vehicleId:300}}
     * /latitude:100/longitude:200/timestamp:150000 - cars{car{vehicleId:100},car{vehicleId:400},car{vehicleId:500}}
     *
     * //vehicleId 400 leaves location(100,200) at 150000
     * /latitude:100/longitude:200/timestamp:150000 - cars{car{vehicleId:100}}
     * //vehicledId 700 comes to location(100,200) at 170000
     * /latitude:100/longitude:200/timestamp:170000 - cars{car{vehicleId:100},car{vehicleId:700}}
     * //vehicleId 600 comes to location(100,300) at 190000
     * /latitude:100/longitude:300/timestamp:190000 - cars{car{vehicleId:200},car{vehicleId:300},car{vehicleId:600}}
     *
     * As seen above from the example this accounts for active tracking of each vehicle at a location and even if it leaves we can tombstone based on its exit
     * and process the downstream consumers accordingly. Its all done for free through Apache Storm.
     *
     * When the API requests for a location, time range and distance.
     * We need to form the start and stop key based on the distance given to us
     * We can easily create a scan on hbase based on the start and stop key and return the grouping of cars based on the value.
     * Since hbase colocates rows lexicographically with enough salting this will give a very good scan performance for the API listed.
     *
     */

    /**
     *
     * Pros: once we find the starting and ending location indices(lat && long) based on the distance(euclidean) the scan will work very
     * efficiently to
     *
     * Cons: In the long run though if we want to run metrics and analysis on it we will need an excellent salting API to back this.
     * This has got potential to hotspot region servers if not salted properly. I don't like to overoptimize on initial design but this
     * will be a way to solve it but another route I have also thought of solving this would be using the OpenTSDB. (http://opentsdb.net/)
     *
     * Since its a model based on timeseries we can create a composite key using timestamp && location index we can gather metrics at all
     * locations based on absolute time and deltas from the absolute time as well.
     *
     *
     *
     *
     */

}
