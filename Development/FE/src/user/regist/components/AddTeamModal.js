import React, { useEffect, useRef } from "react";
import Modal from "../../../shared/components/UIElements/Modal";
import Button from "../../../shared/components/TableInputElements/Button";

import "./AddTeamModal.css";

const AddTeamModal = (props) => {
  const buttonRef = useRef(null); // 버튼에 대한 ref 생성
  const previousFocusRef = useRef(null); // 이전 포커스된 요소를 참조하기 위한 ref
  const show = !!props.error;

  useEffect(() => {
    if (show && buttonRef.current) {
      previousFocusRef.current = document.activeElement; // 모달이 열리기 전 포커스된 요소를 저장
      buttonRef.current.focus(); // 버튼에 포커스를 줍니다
    }
  }, [show]); // show가 변할 때마다 실행

  const onClear = () => {
    props.onClear();
    if (previousFocusRef.current) {
      previousFocusRef.current.focus(); // 이전에 포커스된 요소로 다시 포커스 이동
    }
  };

  return (
    <Modal
      onCancel={onClear}
      headerClass="modal__header-hide"
      show={show}
      footer={
        <Button ref={buttonRef} onClick={onClear}>
          확인
        </Button>
      }
    >
      <div className="error-icon modal__content-flex">
        <WarningIcon htmlColor="#FF0000" fontSize="large" />
        <div className="error-title">
          {props.title ? props.title : "오류 발생 안내"}
        </div>
      </div>
      <div className="error-detail">{props.error}</div>
    </Modal>
  );
};

export default AddTeamModal;
