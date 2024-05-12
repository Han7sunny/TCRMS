import {
  VALIDATOR_REQUIRE,
  VALIDATOR_REQUIRE_LENGTH,
} from "../../shared/util/validators";
import { WEIGHT_ID } from "./const-event";

// individual
export const TABLE_COLUMNS_REGIST_INDIVIDUAL = [
  // { id: "number", name: "", type: "text" },
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
    detail: { items: ["대한민국", "영국", "프랑스"] },
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
