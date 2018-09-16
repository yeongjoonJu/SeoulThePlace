package com.ensharp.seoul.seoultheplace;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import javax.security.auth.login.LoginException;

public class Constant {
    static CourseVO course;
    static PlaceVO place1;
    static PlaceVO place2;
    static PlaceVO place3;
    static List<String> placeCode = new ArrayList<String>();

    static String[] imageURL1 = new String[3];
    static String[] imageURL2 = new String[3];
    static String[] imageURL3 = new String[3];
    static String parkingFee1;
    static String parkingFee2;
    static String parkingFee3;
    static String detail;
    static String detail1;
    static String detail2;
    static String detail3;

    static ArrayList<PlaceVO> places;

    public static void initCourse() {
        detail = "간선버스 273번은 대학로와 신촌을 가로지르는 노선으로 이 버스를 타면 서울 시내 대학교 투어를 할 수 있다. 고려대부터 성균관대, 이화여대, 연세대, 홍익대 등 9개 이상의 대학교를 거친다. 그만큼 젊은 층의 수요도 높은 버스로 대학마크가 박힌 점퍼를 입은 학생들도 쉽게 마주칠 수 있다. 273버스 노선 중 대표적인 학교 3곳을 소개한다.";

        placeCode.add("a111");
        placeCode.add("a222");
        placeCode.add("a333");

        course = new CourseVO("j111", "서울시내 대학 경유 273번 버스로 떠나는 청춘과 낭만의 대학 투어", "친구끼리, 연인끼리",
                5, detail, placeCode, 126.937546, 37.5637563);
    }

    public static void initPlaces() {
        // place1
        imageURL1[0] = "http://korean.visitseoul.net/comm/getImage?srvcId=MEDIA&parentSn=13212&fileTy=MEDIA&fileNo=1";
        imageURL1[1] = "http://korean.visitseoul.net/comm/getImage?srvcId=MEDIA&parentSn=13213&fileTy=MEDIA&fileNo=1";
        imageURL1[2] = "http://www.yonsei.ac.kr/_attach/image/sc/2018/01/7c0becab0546c7252f2ea5524ae81f15.jpg";

        parkingFee1 = "본 30분 2,000원\n(평일 출근시간대 07:30~09:30\n퇴근 시간대 18:00~20:00\n입차 후 최초 30분 3,000원 적용)\n" +
                "30분 이후 2시간까지 10분당 500원씩 할증\n" +
                "입차 후 2시간 이후 10분당 1,000원식 할증";

        detail1 = "연세대학교는 그 전신인 세브란스의과대학과 연희대학교가 1957년 통합되면서 출범했다. 연세대 정문을 들어서면 울창한 나무들 사이로 쭉 뻗은 백양로와 이어진다. 양쪽으로 들어선 연세대의 다양한 건물들을 보며 걷다보면 왼편에 우뚝 서 있는 연세대의 상징 독수리 상을 마주칠 수 있다. 담쟁이덩굴이 고딕풍 석조건물을 휘감고 있는 언어두으관과 양 옆의 스팀슨관, 아펜젤러관 모두 사적으로 지정돼 있다. ㄷ자 모양의 건물 가운데 조성된 영국식 정원과 언더우드 동상을 배경으로 사진을 찍는 사람들의 모습도 심심찮게 볼 수 있다. 언더우드관 뒤편에 있는 연희관(사회과학대학)은 영화 [엽기적인 그녀], [클래식], 드라마 [응답하라 1994] 등 드라마와 영화 촬영지로도 인기가 높은 곳이다. 그 외 윤동주 기념관에서는 대표작 '서시'를 시인의 필체로 조각한 시비를 볼 수 있고, 걷기 좋은 청송대는 연세대 학생들뿐만 아니라 주변 주민들의 사랑을 받고 있다.";

        place1 = new PlaceVO("a111", "연세대학교 신촌캠퍼스", "서울특별시 서대문구 연세로 50", imageURL1, "02-1599-1885",
                "신촌역 나가보기", "있음", parkingFee1, 3, detail1,
                "친구끼리, 연인끼리", "연중무휴", 126.937546, 37.5637563);

        // place2
        imageURL2[0] = "http://korean.visitseoul.net/comm/getImage?srvcId=MEDIA&parentSn=13216&fileTy=MEDIA&fileNo=1";
        imageURL2[1] = "http://korean.visitseoul.net/comm/getImage?srvcId=MEDIA&parentSn=13217&fileTy=MEDIA&fileNo=1";
        imageURL2[2] = "https://s3.orbi.kr/data/file/united/3667685840_sWlau4Gz_EAB2BDEC9881EB8C80.jpg";

        parkingFee2 = "10분~2시간까지 10분당 500원\n" +
                "2시간~6시간까지 10분당 1,000원";

        detail2 = "고려대학교는 1905년에 설립되었다. 고려대 정문을 들어서면 한눈에 보이는 고딕풍 석조건물의 본관은 한국의 1세대 건축가 박동진이 설계했다. 사적으로도 지정된 유서 깊은 건물이다. 그 앞의 탁 트인 넓은 중앙 광장은 학생들의 쉼터이자 만남의 장소다. 중앙광장 지하에는 3층 규모의 광장이 조성돼있다. 실제로 고려대는 한국 최초로 지하캠퍼스를 조성했다. 이곳에는 학생들을 위한 열람실과 편의시설, 카페, 피트니스 센터 등이 갖춰져 있다. 또 고려대학교 식품연구소에서 만든 빵을 파는 ‘고대빵’ 베이커리와 기념품샵도 자리잡고 있다. 100주년 기념관 내에 있는 박물관에서는 국보급문화재를 포함한 다양한 유물뿐만 아니라 박수근, 천경자 등 유명 작가의 그림도 감상할 수 있다.";

        place2 = new PlaceVO("a222", "고려대학교 안암캠퍼스", "서울특별시 성북구 안암로 145", imageURL2, "02-3290-1114",
                "안암역 나가보기", "있음", parkingFee2, 2, detail2,
                "친구끼리, 연인끼리", "연중무휴", 127.032519, 37.5886007);

        // place3
        imageURL3[0] = "http://korean.visitseoul.net/comm/getImage?srvcId=MEDIA&parentSn=13226&fileTy=MEDIA&fileNo=1";
        imageURL3[1] = "http://korean.visitseoul.net/comm/getImage?srvcId=MEDIA&parentSn=13227&fileTy=MEDIA&fileNo=1";
        imageURL3[2] = "http://mblogthumb4.phinf.naver.net/20150608_115/sgycool_1433724758504PXafH_JPEG/1.JPG?type=w2";

        parkingFee3 = "0분 미만 500원\n" +
                "30분 2,000원, 초과 10분당 500원\n" +
                "2시간 초과 10분당 1,000원";

        detail3 = "성균관대학교의 전신은 1398년 조선시대 성균관에서 출발했다. 성균관대 명륜동 캠퍼스는 과거와 현재가 공존하는 곳이다. 정문을 들어서면 한국과 중국의 뛰어난 학자들의 위패를 안치한 문묘 대성전과 조선 최고의 고등교육기관 성균관이 나온다. 유생들이 공부하던 명륜당의 뜰에는 수령 400년이 넘은 것으로 추정되는 은행나무가 있다. 이곳에서는 옛 선인들의 자취를 느끼며 고즈넉한 산책을 즐길 수 있다. 성균관대학교 캠퍼스에 들어서면 제일 먼저 눈에 띄는 건물은 조선시대에 과거 시험을 보던 장소였던 비천당과 600주년기념관이다. 600주년기념관 지하에 있는 성균관대 박물관에서는 유교문화, 성균관의 역사 등 다양한 주제의 전시를 감상할 수 있다. 성균관대 법학관 옥상은 서울 시내가 한눈에 내려다보여 야경 명소로도 손꼽힌다. 또, 드라마 [태양의 후예]로 큰 사랑을 받은 배우 송중기의 모교이기도 하다.";

        place3 = new PlaceVO("a333", "성균관대학교 인문사회과학캠퍼스", "서울특별시 종로구 성균관로 25-2", imageURL3, "02-760-0114",
                "혜화역 나가보기", "있음", parkingFee3, 2, detail3,
                "친구끼리, 연인끼리", "연중무휴", 126.992064, 37.5884693);

        places = new ArrayList<PlaceVO>();
        places.add(place1);
        places.add(place2);
        places.add(place3);;
    }

    public static CourseVO getCourse() { return course; }

    public static ArrayList<PlaceVO> getPlaces(String placeCode) { return places; }

    public static PlaceVO getPlace(String code) {
        if (code.equals("a111")) return places.get(0);
        else if (code.equals("a222")) return places.get(1);
        else if (code.equals("a333")) return places.get(2);
        else return null;
    }
}
