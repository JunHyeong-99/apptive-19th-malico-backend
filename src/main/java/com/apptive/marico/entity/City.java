package com.apptive.marico.entity;

public enum City {
    AllRegions("모든 지역"),
    SeoulSpecialCity("서울특별시"),
    BusanMetropolitanCity("부산광역시"),
    DaeguMetropolitanCity("대구광역시"),
    IncheonMetropolitanCity("인천광역시"),
    GwangjuMetropolitanCity("광주광역시"),
    DaejeonMetropolitanCity("대전광역시"),
    UlsanMetropolitanCity("울산광역시"),
    SejongSpecialSelfGoverningCity("세종특별자치시"),
    GyeonggiProvince("경기도"),
    ChungcheongbukProvince("충청북도"),
    ChungcheongnamProvince("충청남도"),
    JeollabukProvince("전라북도"),
    JeollanamProvince("전라남도"),
    GyeongsangbukProvince("경상북도"),
    GyeongsangnamProvince("경상남도"),
    JejuSpecialSelfGoverningProvince("제주특별자치도"),
    GangwonSpecialSelfGoverningProvince("강원특별자치도");

    private final String displayName;

    City(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static City fromDisplayName(String displayName) {
        for (City city : City.values()) {
            if (city.getDisplayName().equals(displayName)) {
                return city;
            }
        }
        throw new IllegalArgumentException("적절하지 않은 도시 입니다. " + displayName);
    }
}
