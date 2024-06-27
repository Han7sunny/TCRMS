import React, { useEffect, useRef, useState } from "react";

import "./File.css";

const File = (props) => {
  const fileInputRef = useRef(null);
  const [fileName, setFileName] = useState("");

  const handleFileChange = async (event) => {
    event.preventDefault();

    const files = event.target.files;
    if (files.length) {
      if (props.multiple) {
        await props.onChange(files, event.target.id); // 선택한 파일 배열을 부모 컴포넌트에 전달합니다.
      } else {
        const file = files[0];
        setFileName(file.name);
        await props.onChange(file, event.target.id); // 선택한 파일 배열을 부모 컴포넌트에 전달합니다.
      }
    }
  };

  const handleClick = () => {
    fileInputRef.current.click();
  };

  useEffect(() => {
    if (props.fileName) {
      setFileName(props.fileName);
    }
  }, [props.fileName]);

  return (
    <div className="file-upload">
      <div className="button-center">
        <button className="file-upload-button" onClick={handleClick}>
          {props.content}
        </button>
      </div>
      <div className="file-name">{fileName}</div>
      <input
        id={props.id}
        type="file"
        ref={fileInputRef}
        onChange={handleFileChange}
        style={{ display: "none" }}
        multiple={props.multiple}
        accept={props.accept}
      />
    </div>
  );
};

export default File;
