import {
  VALIDATOR_REQUIRE,
  VALIDATOR_REQUIRE_LENGTH,
} from "../../shared/util/validators";
import { WEIGHT_ID } from "./const-event";

const nationalityCandidate = [
  "국적선택",
  "가나",
  "가봉",
  "가이아나",
  "건지(영)",
  "과달루프",
  "과테말라",
  "괌(미)",
  "그레나다",
  "그루지야",
  "그리스",
  "그린란드(덴)",
  "기니",
  "기니비사우",
  "기아나(프)",
  "나미비아",
  "나우루",
  "나이지리아",
  "남극",
  "남수단공화국",
  "남아프리카공화국",
  "네덜란드",
  "네팔",
  "노르웨이",
  "노포크섬(오)",
  "뉴질랜드",
  "뉴칼레도니아(프)",
  "니우에(뉴)",
  "니제르",
  "니카라과",
  "대만",
  "덴마크",
  "도미니카공화국",
  "도미니카연방",
  "독일",
  "동티모르",
  "라오스",
  "라이베리아",
  "라트비아",
  "러시아",
  "레바논",
  "레소토",
  "레위니옹(프)",
  "루마니아",
  "룩셈부르크",
  "르완다",
  "리비아",
  "리투아니아",
  "리히텐슈타인",
  "마다가스카르",
  "마르티니크",
  "마샬군도",
  "마요트(프)",
  "마이크로네시아",
  "마카오",
  "마케도니아",
  "말라위",
  "말레이시아",
  "말리",
  "맨섬(영)",
  "멕시코",
  "모나코",
  "모로코",
  "모르타니아",
  "모리셔스",
  "모잠비크",
  "몬세라트(영)",
  "몬테네그로",
  "몰도바",
  "몰디브",
  "몰타",
  "몽골",
  "미국",
  "미국령 군소제도",
  "미얀마",
  "바누아투",
  "바레인",
  "바베이도스",
  "바티칸",
  "바하마",
  "방글라데시",
  "버뮤다(영)",
  "버진제도(미)",
  "버진제도(영)",
  "베냉",
  "베네수엘라",
  "베트남",
  "벨기에",
  "벨로루시",
  "벨리즈",
  "보네르신트유스타티우스사바(네)",
  "보빗군도",
  "보스니아헤르체코비나",
  "보츠와나",
  "볼리비아",
  "부룬디",
  "부르키나파소",
  "부탄",
  "북마리아나제도(미)",
  "북한",
  "불가리아",
  "브라질",
  "브루나이",
  "사모아",
  "사모아(미)",
  "사우디아라비아",
  "사우스조지아 사우스샌드위치섬",
  "산마리노",
  "상투메프린시페",
  "생바르텔레미",
  "서사하라",
  "세네갈",
  "세르비아",
  "세이셀",
  "세인트루시아",
  "세인트마틴섬",
  "세인트마틴섬(네)",
  "세인트빈센트그레나딘",
  "세인트키츠네비스",
  "세인트피에르미켈론(프)",
  "세인트헬레나(영)",
  "소말리아",
  "솔로몬제도",
  "수단",
  "수리남",
  "스리랑카",
  "스발바르 얀먀옌",
  "스와질랜드",
  "스웨덴",
  "스위스",
  "스페인",
  "슬로바키아",
  "슬로베니아",
  "시리아",
  "시에라리온",
  "싱가포르",
  "아랍에미레이트연합",
  "아루바(네)",
  "아르메니아",
  "아르헨티나",
  "아이슬랜드",
  "아이티",
  "아일랜드",
  "아제르바이잔",
  "아프가니스탄",
  "안길라(영)",
  "안도라",
  "알바니아",
  "알제리",
  "앙골라",
  "앤티바가부다",
  "앤틸리스제도(네)",
  "에리트레아",
  "에스토니아",
  "에콰도르",
  "에티오피아",
  "엘살바도르",
  "영국",
  "영국령인도양식민지",
  "예맨",
  "오만",
  "오스트리아",
  "온두라스",
  "올랜드제도(핀)",
  "요르단",
  "우간다",
  "우루과이",
  "우즈베키스탄",
  "우크라이나",
  "월리스푸투나(프)",
  "이라크",
  "이란",
  "이스라엘",
  "이집트",
  "이탈리아",
  "인도",
  "인도네시아",
  "일본",
  "자메이카",
  "잠비아",
  "잠비아",
  "저지제도(영)",
  "적도기니",
  "전세계",
  "중국",
  "중앙아프리카공화국",
  "지부티",
  "지브롤터(영)",
  "짐바브웨",
  "차드",
  "체코",
  "칠레",
  "카메룬",
  "카보베르데",
  "카자흐스탄",
  "카타르",
  "캄보디아",
  "캐나다",
  "케냐",
  "케이맨제도(영)",
  "코모로",
  "코스타리카",
  "코코스제도(호)",
  "코트디부아르",
  "콜롬비아",
  "콩고",
  "콩고민주공화국",
  "쿠바",
  "쿠웨이트",
  "쿡제도(뉴)",
  "퀴라소(네)",
  "크로아티아",
  "크리스마스섬(호)",
  "키르기스스탄",
  "키리바시",
  "키프로스",
  "타지키스탄",
  "탄자니아",
  "태국",
  "터크스케이커스제도(영)",
  "터키",
  "토고",
  "토켈라우제도(뉴)",
  "투르크메니스탄",
  "투발루",
  "퉁가",
  "튀니지",
  "트리니다드토바고",
  "파나마",
  "파라과이",
  "파키스탄",
  "파푸아뉴기니",
  "팔라우",
  "팔레스타인거주지",
  "페로스제도(덴)",
  "페루",
  "포르투갈",
  "포클랜드제도",
  "폴란드",
  "폴리네시아(프)",
  "푸에르토리코(미)",
  "프랑스",
  "프랑스령남방영토",
  "피지",
  "핀란드",
  "필리핀",
  "핏케언(영)",
  // "한국",
  "허드섬 맥도날드제도",
  "헝가리",
  "호주",
  "홍콩",
];

// individual
export const TABLE_COLUMNS_REGIST_INDIVIDUAL = [
  {
    id: "name",
    name: "성명",
    type: "input",
    detail: { validators: [VALIDATOR_REQUIRE()], placeholder: "성명" },
  },
  {
    id: "sex",
    name: "성별",
    type: "radio-group",
    detail: {
      items: ["남성", "여성"],
      showLabel: true,
      affector: { id: "-col6-weight", type: "setting", value: WEIGHT_ID },
    },
  },
  {
    id: "foreigner",
    name: "외국인",
    type: "checkbox-group",
    detail: {
      items: ["외국인"],
      showLabel: true,
      affector: { id: "-col3-nationality", type: "disabled", value: "외국인" },
    },
  },
  {
    id: "nationality",
    name: "국적",
    type: "dropdown",
    detail: { items: nationalityCandidate },
  },
  {
    id: "idnumber",
    name: "주민등록번호",
    type: "multi-input",
    detail: [
      {
        id: "idnumber-input0",
        type: "input",
        detail: { validators: [VALIDATOR_REQUIRE_LENGTH(6)] },
      },
      { id: "idnum-hypen", type: "text" },
      {
        id: "idnumber-input2",
        type: "input",
        detail: { validators: [VALIDATOR_REQUIRE_LENGTH(7)] },
      },
    ],
  },
  {
    id: "event",
    name: "종목",
    type: "checkbox-group",
    detail: {
      items: ["겨루기", "품새"],
      showLabel: true,
      affector: { id: "-col6-weight", type: "disabled", value: "겨루기" },
    },
  },
  {
    id: "weight",
    name: "체급(겨루기만)",
    type: "dropdown",
    detail: {
      items: [
        "체급선택",
        "핀",
        "플라이",
        "밴텀",
        "페더",
        "라이트",
        "웰터",
        "미들",
        "헤비",
      ],
    },
  },
  { id: "delete-btn", name: "", type: "button", detail: { content: "삭제" } },
];

export const TABLE_COLUMNS_CHECK_INDIVIDUAL = [
  { id: "name", name: "성명", type: "text" },
  { id: "sex", name: "성별", type: "text" },
  { id: "foreigner", name: "외국인", type: "text" },
  { id: "nationality", name: "국적", type: "text" },
  {
    id: "idnumber",
    name: "주민등록번호",
    type: "text-hidden",
    detail: { showCharNum: 7 },
  },
  { id: "event", name: "종목", type: "text" },
  { id: "weight", name: "체급(겨루기만)", type: "text" },
];

// second
export const TABLE_COLUMNS_REGIST_SECOND = [
  {
    id: "name",
    name: "성명",
    type: "input",
    detail: { validators: [VALIDATOR_REQUIRE()], placeholder: "성명" },
  },
  {
    id: "sex",
    name: "성별",
    type: "radio-group",
    detail: {
      items: ["남성", "여성"],
      showLabel: true,
    },
  },
  {
    id: "foreigner",
    name: "외국인",
    type: "checkbox-group",
    detail: {
      items: ["외국인"],
      showLabel: true,
      affector: { id: "-col3-nationality", type: "disabled", value: "외국인" },
    },
  },
  {
    id: "nationality",
    name: "국적",
    type: "dropdown",
    detail: { items: nationalityCandidate },
  },
  {
    id: "idnumber",
    name: "주민등록번호",
    type: "multi-input",
    detail: [
      {
        id: "idnumber-input0",
        type: "input",
        detail: { validators: [VALIDATOR_REQUIRE_LENGTH(6)] },
      },
      { id: "idnum-hypen", type: "text" },
      {
        id: "idnumber-input2",
        type: "input",
        detail: { validators: [VALIDATOR_REQUIRE_LENGTH(7)] },
      },
    ],
  },
  { id: "delete-btn", name: "", type: "button", detail: { content: "삭제" } },
];

export const TABLE_COLUMNS_CHECK_SECOND = [
  { id: "name", name: "성명", type: "text" },
  { id: "sex", name: "성별", type: "text" },
  { id: "foreigner", name: "외국인", type: "text" },
  { id: "nationality", name: "국적", type: "text" },
  {
    id: "idnumber",
    name: "주민등록번호",
    type: "text-hidden",
    detail: { showCharNum: 7 },
  },
];
