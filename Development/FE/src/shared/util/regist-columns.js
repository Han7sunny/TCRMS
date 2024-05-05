// individual
export const TABLE_COLUMNS_REGIST_INDIVIDUAL = [
  { id: "number", name: "", type: "text" },
  {
    id: "name",
    name: "성명",
    type: "input",
    detail: { validators: "[]", placeholder: "성명" },
  },
  {
    id: "sex",
    name: "성별",
    type: "radio-group",
    detail: { items: ["남성", "여성"], initialValue: "남성", showLabel: true },
  },
  {
    id: "foreigner",
    name: "외국인",
    type: "checkbox-group",
    detail: { items: ["외국인"], showLabel: true },
  },
  {
    id: "nationality",
    name: "국적",
    type: "dropdown",
    detail: { items: ["대한민국", "영국", "프랑스"] },
  },
  {
    id: "idnumber",
    name: "주민등록번호",
    type: "multi-input",
    details: [
      { id: "idnum1", type: "input", detail: {} },
      { id: "idnum-hypen", type: "text", detail: { content: "-" } },
      { id: "idnum1", type: "input", detail: {} },
    ],
  },
  {
    id: "event",
    name: "종목",
    type: "checkbox-group",
    detail: { items: ["겨루기", "품새"], showLabel: true },
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
