// - 개인전(1,2,3,4)
//     - 개인전 여자 겨루기 1
//     - 개인전 여자 품새 2
//     - 개인전 남자 겨루기 3
//     - 개인전 남자 품새 4
// - 단체전(5,6,7,8)
//     - 단체전 여자 겨루기 5
//     - 단체전 여자 품새 6
//     - 단체전 남자 겨루기 7
//     - 단체전 남자 품새 8
// - 혼성(9)
// - 세컨(10)
// - 자원봉사자(11)

export const EVENT_ID = {
  "개인전 여자 겨루기": 1,
  "개인전 여자 품새": 2,
  "개인전 남자 겨루기": 3,
  "개인전 남자 품새": 4,
  "단체전 여자 겨루기": 5,
  "단체전 여자 품새": 6,
  "단체전 남자 겨루기": 7,
  "단체전 남자 품새": 8,
  "단체전 혼성 품새": 9,
  세컨: 10,
  자원봉사자: 11,
  1: ["개인전", "여자", "겨루기"],
  2: ["개인전", "여자", "품새"],
  3: ["개인전", "남자", "겨루기"],
  4: ["개인전", "남자", "품새"],
  5: ["단체전", "여자", "겨루기"],
  6: ["단체전", "여자", "품새"],
  7: ["단체전", "남자", "겨루기"],
  8: ["단체전", "남자", "품새"],
  9: ["단체전", "혼성", "품새"],
  10: ["세컨"],
  11: ["자원봉사자"],
};

export const WEIGHT_ID = {
  // 핀(1)/플라이(2)/벤텀(3)/페더(4)/라이트(5)/웰터(6)/미들(7)/헤비(8)/미들헤비(9)
  남성: {
    핀: 1,
    플라이: 2,
    벤텀: 3,
    페더: 4,
    라이트: 5,
    웰터: 6,
    미들: 7,
    헤비: 8,
  },
  여성: {
    핀: 1,
    플라이: 2,
    벤텀: 3,
    페더: 4,
    라이트: 5,
    웰터: 6,
    미들헤비: 9,
  },
};