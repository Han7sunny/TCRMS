import React, { useEffect, useRef } from "react";

import Modal from "./Modal";
import Button from "../FormElements/Button";
import ErrorOutlineOutlinedIcon from "@material-ui/icons/ErrorOutlineOutlined";

import "./ErrorModal.css";

const ErrorModal = (props) => {
  const buttonRef = useRef(null); // 버튼에 대한 ref 생성
  const show = !!props.error;

  useEffect(() => {
    // 에러가 있고, 모달이 보여질 때 버튼에 포커스를 줍니다.
    if (show && buttonRef.current) {
      buttonRef.current.focus();
    }
  }, [show]); // show가 변할 때마다 실행

  return (
    <Modal
      onCancel={props.onClear}
      headerClass="modal__header-hide"
      show={show}
      footer={
        <Button ref={buttonRef} onClick={props.onClear}>
          확인
        </Button>
      }
    >
      <div className="error-icon modal__content-flex">
        <ErrorOutlineOutlinedIcon htmlColor="#FFD400" fontSize="large" />
        <div className="error-title">
          {props.title ? props.title : "오류 발생 안내"}
        </div>
      </div>
      <div className="error-detail">{props.error}</div>
    </Modal>
  );
};

export default ErrorModal;
