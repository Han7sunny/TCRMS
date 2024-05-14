export const checkValidityIndividual = (participant) => {
  const { name, sex, idnumber, event, weight } = participant;

  if (!name) {
    return {
      result: false,
      message: "이름을 입력해주세요.",
      focusCol: "-col0-name",
    };
  }
  if (!sex) {
    return {
      result: false,
      message: "성별을 선택해주세요.",
      focusCol: "-col1-sex",
    };
  }
  if (!event.length) {
    return {
      result: false,
      message: "종목을 한 개 이상 선택해주세요.",
      focusCol: "-col5-event",
    };
  }

  if (idnumber[0] || idnumber[2]) {
    if (/[^0-9]/.test(idnumber[0])) {
      return {
        result: false,
        message: "주민번호에 숫자만 입력해주세요.",
        focusCol: "-col4-idnumber-input0",
      };
    }
    if (/[^0-9]/.test(idnumber[2])) {
      return {
        result: false,
        message: "주민번호에 숫자만 입력해주세요.",
        focusCol: "-col4-idnumber-input2",
      };
    }
    if (idnumber[0].length !== 6) {
      return {
        result: false,
        message: "주민번호 자리수가 맞지 않습니다.",
        focusCol: "-col4-idnumber-input0",
      };
    }
    if (idnumber[2].length !== 7) {
      return {
        result: false,
        message: "주민번호 자리수가 맞지 않습니다.",
        focusCol: "-col4-idnumber-input2",
      };
    }
    if (
      (sex === "남성" && [1, 3].includes(idnumber[2][0])) ||
      (sex === "여성" && [2, 4].includes(idnumber[2][0]))
    ) {
      return {
        result: false,
        message: "성별과 주민번호가 일치하지 않습니다.",
        focusCol: "-col4-idnumber-input2",
      };
    }
  }

  if (event.includes("겨루기")) {
    if (!weight || weight === "체급선택") {
      return {
        result: false,
        message: "체급을 선택해주세요.",
        focusCol: "-col6-weight",
      };
    }
  }

  return { result: true };
};
