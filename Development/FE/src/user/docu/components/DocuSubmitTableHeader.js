import React from "react";
import { deepEqual } from "../../../shared/util/deepEqual";

function areEqual(prevProps, nextProps) {
  // 같으면 true 다르면 false
  return (
    prevProps.hideText === nextProps.hideText &&
    deepEqual(prevProps.columns, nextProps.columns)
  );
}

const DocuSubmitTableHeader = React.memo(
  ({ flatColumns, showNumber, columns, setHideText, hideText }) => {
    console.log("DOCU_TABLE_HEADER");
    return (
      <React.Fragment>
        <colgroup>
          {flatColumns.map((col, i) => (
            <col key={col.id} className={"table-col-" + i} />
          ))}
          {showNumber && <col className={"table-col-" + flatColumns.length} />}
        </colgroup>
        <thead>
          <tr>
            {showNumber && <th rowSpan={2}></th>}
            {columns.map((col) => {
              if (col.columns) {
                return (
                  <th key={col.id} rowSpan={1} colSpan={col.columns.length}>
                    {col.name}
                  </th>
                );
              } else {
                if (col.type === "text-hidden") {
                  return (
                    <th key={col.id} rowSpan={2}>
                      {col.name}{" "}
                      <button
                        className="table-column__hide-btn"
                        onClick={() => {
                          setHideText(!hideText);
                        }}
                      >
                        {hideText ? "보이기" : "숨기기"}
                      </button>
                    </th>
                  );
                } else {
                  return (
                    <th key={col.id} rowSpan={2}>
                      {col.name}
                    </th>
                  );
                }
              }
            })}
          </tr>
          <tr>
            {columns
              .filter((col) => col.columns)
              .map((col) =>
                col.columns.map((col) => (
                  <th key={col.id} rowSpan={1}>
                    {col.name}
                  </th>
                ))
              )}
          </tr>
        </thead>
      </React.Fragment>
    );
  },
  areEqual
);

export default DocuSubmitTableHeader;
