export const colInfo = [
  { id: "name", name: "성명", type: "text" },
  {
    id: "identityNumber",
    name: "주민등록번호",
    type: "text-hidden",
    detail: { showCharNum: 7 },
  },
  { id: "types", name: "유형", type: "text", detail: { separator: ", " } },
  { id: "events", name: "종목", type: "text", detail: { separator: ", " } },
  {
    id: "documents",
    name: "제출 서류",
    columns: [
      { id: "docu1", name: "증명사진", type: "submit-button" },
      { id: "docu2", name: "서약서", type: "submit-button" },
      { id: "docu3", name: "선수등록이력", type: "submit-button" },
      { id: "docu4", name: "학적부", type: "submit-button" },
      { id: "docu5", name: "외국인등록증", type: "submit-button" },
    ],
  },
  {
    id: "docu-allSubmit",
    name: "서류 일괄제출",
    type: "submit-button-multiple",
  },
];

export const colInfoVolunteer = [
  { id: "name", name: "성명", type: "text" },
  // {
  //   id: "identityNumber",
  //   name: "주민등록번호",
  //   type: "text-hidden",
  //   detail: { showCharNum: 7 },
  // },
  { id: "types", name: "유형", type: "text", detail: { separator: ", " } },
  {
    id: "documents",
    name: "제출 서류",
    columns: [{ id: "docu1", name: "증명사진", type: "submit-button" }],
  },
  // { id: "docu-allSubmit", name: "서류 일괄제출", type: "submit-button" },
];
