export const checkValidityIndividual = (participant) => {
  const {
    name,
    sex,
    idnumber,
    foreigner,
    nationality,
    event,
    weight,
    phoneNumber,
  } = participant;

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

  if (foreigner.join("") === "외국인") {
    if (nationality === "국적선택" || !nationality) {
      return {
        result: false,
        message: "외국인 선수의 국적을 선택해주세요.",
        focusCol: "-col3-nationality",
      };
    }

    if (!phoneNumber) {
      // 주민번호가 잘 다 들어가 있는지 체크
      // 주민번호 입력되지 않았다면, 주민번호나 비고란(개인식별을 위한 폰번호 또는 이메일)을 채워주세요.
      // 주민번호 자릿수 안맞으면 맞추라하기
      const { result, message, focusCol } = checkIdNumber(
        idnumber[0],
        idnumber[2],
        "-col4-idnumber-input0",
        "-col4-idnumber-input2",
        "주민번호(외국인등록번호)나 비고란(외국인등록번호가 없는 경우 폰번호 또는 이메일주소) 중 하나를 입력해주세요."
      );
      if (!result) {
        return { result, message, focusCol };
      }
    } else {
      // 주민번호가 비어져있어야함
      // 아닌 경우 한가지만 입력하라고 메세지 주기
      if (idnumber[0] || idnumber[2]) {
        return {
          result: false,
          message: "주민번호(외국인등록번호)나 비고란 중 하나만 입력해주세요.",
          focusCol: "-col7-phoneNumber",
        };
      }
    }
  } else {
    const { result, message, focusCol } = checkIdNumber(
      idnumber[0],
      idnumber[2],
      "-col4-idnumber-input0",
      "-col4-idnumber-input2",
      "주민번호를 입력해주세요."
    );
    if (!result) {
      return { result, message, focusCol };
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

export const checkValidityTeam = (participant, event) => {
  const { name, sex, idnumber, foreigner, nationality, weight } = participant;

  if (!name) {
    return {
      result: false,
      message: "이름을 입력해주세요.",
      focusCol: "-col1-name",
    };
  }
  if (!sex) {
    return {
      result: false,
      message: "성별을 선택해주세요.",
      focusCol: "-col2-sex",
    };
  }

  if (foreigner.join("") === "외국인") {
    if (nationality === "국적선택" || !nationality) {
      return {
        result: false,
        message: "외국인 선수의 국적을 선택해주세요.",
        focusCol: "-col4-nationality",
      };
    }
  } else {
    if (!idnumber[0]) {
      return {
        result: false,
        message: "주민번호를 입력해주세요.",
        focusCol: "-col5-idnumber-input0",
      };
    }
    if (!idnumber[2]) {
      return {
        result: false,
        message: "주민번호를 입력해주세요.",
        focusCol: "-col5-idnumber-input2",
      };
    }
  }

  if (idnumber[0] || idnumber[2]) {
    if (/[^0-9]/.test(idnumber[0])) {
      return {
        result: false,
        message: "주민번호에 숫자만 입력해주세요.",
        focusCol: "-col5-idnumber-input0",
      };
    }
    if (/[^0-9]/.test(idnumber[2])) {
      return {
        result: false,
        message: "주민번호에 숫자만 입력해주세요.",
        focusCol: "-col5-idnumber-input2",
      };
    }
    if (idnumber[0].length !== 6) {
      return {
        result: false,
        message: "주민번호 자리수가 맞지 않습니다.",
        focusCol: "-col5-idnumber-input0",
      };
    }
    if (idnumber[2].length !== 7) {
      return {
        result: false,
        message: "주민번호 자리수가 맞지 않습니다.",
        focusCol: "-col5-idnumber-input2",
      };
    }
    if (
      (sex === "남성" && [1, 3].includes(idnumber[2][0])) ||
      (sex === "여성" && [2, 4].includes(idnumber[2][0]))
    ) {
      return {
        result: false,
        message: "성별과 주민번호가 일치하지 않습니다.",
        focusCol: "-col5-idnumber-input2",
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

export const checkValiditySecond = (participant) => {
  const { name, sex, idnumber, foreigner, nationality, phoneNumber } =
    participant;

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

  if (foreigner.join("") === "외국인") {
    if (nationality === "국적선택" || !nationality) {
      return {
        result: false,
        message: "외국인 선수의 국적을 선택해주세요.",
        focusCol: "-col3-nationality",
      };
    }

    if (!phoneNumber) {
      // 주민번호가 잘 다 들어가 있는지 체크
      // 주민번호 입력되지 않았다면, 주민번호나 비고란(개인식별을 위한 폰번호 또는 이메일)을 채워주세요.
      // 주민번호 자릿수 안맞으면 맞추라하기
      const { result, message, focusCol } = checkIdNumber(
        idnumber[0],
        idnumber[2],
        "-col4-idnumber-input0",
        "-col4-idnumber-input2",
        "주민번호(외국인등록번호)나 비고란(외국인등록번호가 없는 경우 폰번호 또는 이메일주소) 중 하나를 입력해주세요."
      );
      if (!result) {
        return { result, message, focusCol };
      }
    } else {
      // 주민번호가 비어져있어야함
      // 아닌 경우 한가지만 입력하라고 메세지 주기
      if (idnumber[0] || idnumber[2]) {
        return {
          result: false,
          message: "주민번호(외국인등록번호)나 비고란 중 하나만 입력해주세요.",
          focusCol: "-col5-phoneNumber",
        };
      }
    }
  } else {
    const { result, message, focusCol } = checkIdNumber(
      idnumber[0],
      idnumber[2],
      "-col4-idnumber-input0",
      "-col4-idnumber-input2",
      "주민번호를 입력해주세요."
    );
    if (!result) {
      return { result, message, focusCol };
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

  return { result: true };
};

export const checkValidityVolunteer = (participant) => {
  const { name, sex, phoneNumber } = participant;

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
  if (!phoneNumber[0]) {
    return {
      result: false,
      message: "전화번호를 입력해주세요.",
      focusCol: "-col2-phoneNumber-input0",
    };
  }
  if (!phoneNumber[2]) {
    return {
      result: false,
      message: "전화번호를 입력해주세요.",
      focusCol: "-col2-phoneNumber-input2",
    };
  }
  if (!phoneNumber[4]) {
    return {
      result: false,
      message: "전화번호를 입력해주세요.",
      focusCol: "-col2-phoneNumber-input4",
    };
  }

  if (/[^0-9]/.test(phoneNumber[0])) {
    return {
      result: false,
      message: "전화번호에 숫자만 입력해주세요.",
      focusCol: "-col2-phoneNumber-input0",
    };
  }
  if (/[^0-9]/.test(phoneNumber[2])) {
    return {
      result: false,
      message: "전화번호에 숫자만 입력해주세요.",
      focusCol: "-col2-phoneNumber-input2",
    };
  }
  if (/[^0-9]/.test(phoneNumber[4])) {
    return {
      result: false,
      message: "전화번호에 숫자만 입력해주세요.",
      focusCol: "-col2-phoneNumber-input4",
    };
  }
  if (phoneNumber[0].length !== 3) {
    return {
      result: false,
      message: "전화번호 자리수가 맞지 않습니다.",
      focusCol: "-col2-phoneNumber-input0",
    };
  }
  if (phoneNumber[2].length !== 4) {
    return {
      result: false,
      message: "전화번호 자리수가 맞지 않습니다.",
      focusCol: "-col2-phoneNumber-input2",
    };
  }
  if (phoneNumber[4].length !== 4) {
    return {
      result: false,
      message: "전화번호 자리수가 맞지 않습니다.",
      focusCol: "-col2-phoneNumber-input4",
    };
  }

  return { result: true };
};

const checkIdNumber = (
  idNumber1,
  idNumber2,
  focusCol1,
  focusCol2,
  messageNoIdNumber
) => {
  if (!idNumber1) {
    return {
      result: false,
      message: messageNoIdNumber,
      focusCol: focusCol1,
    };
  }
  if (!idNumber2) {
    return {
      result: false,
      message: messageNoIdNumber,
      focusCol: focusCol2,
    };
  }

  if (idNumber1 || idNumber2) {
    if (/[^0-9]/.test(idNumber1)) {
      return {
        result: false,
        message: "주민번호에 숫자만 입력해주세요.",
        focusCol: focusCol1,
      };
    }
    if (/[^0-9]/.test(idNumber2)) {
      return {
        result: false,
        message: "주민번호에 숫자만 입력해주세요.",
        focusCol: focusCol2,
      };
    }
    if (idNumber1.length !== 6) {
      return {
        result: false,
        message: "주민번호 자리수가 맞지 않습니다.",
        focusCol: focusCol1,
      };
    }
    if (idNumber2.length !== 7) {
      return {
        result: false,
        message: "주민번호 자리수가 맞지 않습니다.",
        focusCol: focusCol2,
      };
    }
  }

  return { result: true };
};
