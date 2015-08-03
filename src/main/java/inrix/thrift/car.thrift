struct Car {
    1: required string vehicledId;
    2: required double latitude;
    3: required double longitude;
    4: optional i64 timestamp;
}

struct Cars{
    1: optional set<Car> cars;
}