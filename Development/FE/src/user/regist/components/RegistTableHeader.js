import React from "react";

import Tooltip from "@material-ui/core/Tooltip";

const RegistTableHeader = React.memo(
  ({ showNumber, columns, hideText, setHideText, isEditable }) => {
    return (
      <React.Fragment>
        <colgroup>
          {columns.map((col, i) => (
            <col key={col.id} className={"table-col-" + i} />
          ))}
          {showNumber && <col className={"table-col-" + columns.length} />}
          {isEditable && <col className="table-col-btn" />}
        </colgroup>
        <thead>
          <tr>
            {showNumber && <th></th>}
            {columns.map((col) => {
              if (col.type === "text-hidden") {
                return (
                  <th key={col.id}>
                    {col.name}{" "}
                    <button
                      className="table-column__hide-btn"
                      onClick={() => {
                        setHideText((prevValue) => !prevValue);
                      }}
                    >
                      {hideText ? "보이기" : "숨기기"}
                    </button>
                  </th>
                );
              } else if (col.name === "비고") {
                return (
                  <th key={col.id}>
                    {col.name}
                    <Tooltip
                      title={
                        <div className="table-comments">
                          외국인 선수이며{" "}
                          <span className="info-highlight-case">
                            외국인등록번호가 없는 경우
                          </span>
                          <br />
                          개인식별을 위해{" "}
                          <span className="info-highlight">폰번호</span>나{" "}
                          <span className="info-highlight">이메일 주소</span>{" "}
                          기입
                        </div>
                      }
                      placement="top"
                    >
                      <img
                        src={`${process.env.PUBLIC_URL}/img/info_24dp.png`}
                        width={"14px"}
                        alt="비고"
                      />
                    </Tooltip>{" "}
                  </th>
                );
              } else {
                return <th key={col.id}>{col.name}</th>;
              }
            })}
            {isEditable && <th></th>}
          </tr>
        </thead>
      </React.Fragment>
    );
  }
);

export default RegistTableHeader;
