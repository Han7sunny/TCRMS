import React, { useCallback, useEffect, useState, useContext } from "react";

import { HttpContext } from "../../../shared/context/http-context";
import { AuthContext } from "../../../shared/context/auth-context";

import DocuSubmitTable from "../components/DocuSubmitTable";
import {
  colInfo,
  colInfoVolunteer,
} from "../../../shared/util/submit/submit-columns";

import "./DocuSubmit.css";

const checkValidityFile = (file) => {
  // file type
  const fileType = file.type;
  if (!fileType.includes("image") && !fileType.includes("pdf")) {
    return {
      result: false,
      message: (
        <div>
          해당 파일은 이미지 파일이 아닙니다.
          <br />
          이미지(JPG,JPEG,PNG)나 PDF 파일을 업로드 해주세요.
        </div>
      ),
    };
  }

  // file size
  const fileSize = file.size;
  const limitsize = 1024 ** 2 * process.env.REACT_APP_FILE_LIMIT_SIZE_MB; // 10MB

  if (fileSize > limitsize) {
    return {
      result: false,
      message: (
        <div>
          {process.env.REACT_APP_FILE_LIMIT_SIZE_MB}MB 이하로 파일을
          업로드해주세요.
        </div>
      ),
    };
  }

  return { result: true };
};

const documentsColumn = colInfo.find((col) => col.id === "documents");
const documentsName = documentsColumn.columns.map((col) => col.name);
// const documentsName = ["증명사진", "서약서", "선수등록이력"];

const checkValidityFileMultiple = (file) => {
  // file name
  const fileName = file.name.toString();
  // const fileSplits = fileName.split(/_|\./);
  const fileSplits = fileName.split(".")[0].split("_");
  //  대체 왜 달 라 라아러아러ㅣ나어리나어리ㅏ널
  console.log(fileName);
  console.log(fileName.length);
  console.log(fileSplits);
  console.log(documentsName[1]);
  console.log("서약서" === documentsName[1]);
  console.log(fileName.substr(10, 7) === documentsName[1]);
  console.log(documentsName.indexOf("서약서"));

  if (!documentsName.includes("서약서")) {
    return {
      result: false,
      message: (
        <div>
          "{fileName}"의 파일명 형식이 일치하지 않습니다.
          <br />
          일괄제출 시에는 "[이름]_[서류명]" 으로 파일명을 수정해주세요. (예시 :
          홍길동_증명사진)
        </div>
      ),
    };
  }

  // file type
  const fileType = file.type;
  if (!fileType.includes("image") && !fileType.includes("pdf")) {
    return {
      result: false,
      message: (
        <div>
          해당 파일은 이미지 파일이 아닙니다.
          <br />
          이미지(JPG,JPEG,PNG)나 PDF 파일을 업로드 해주세요.
        </div>
      ),
    };
  }

  // file size
  const fileSize = file.size;
  const limitsize = 1024 ** 2 * process.env.REACT_APP_FILE_LIMIT_SIZE_MB; // 10MB

  if (fileSize > limitsize) {
    return {
      result: false,
      message: (
        <div>
          {process.env.REACT_APP_FILE_LIMIT_SIZE_MB}MB 이하로 파일을
          업로드해주세요.
        </div>
      ),
    };
  }

  return { result: true };
};

const DocuSubmit = () => {
  const notification = (
    <React.Fragment>
      일괄제출할 경우에는 파일명을 아래와 같이 형식에 맞추어 제출바랍니다.{" "}
      <br></br>예) 홍길동_증명사진, 홍길동_서약서, 홍길동_선수등록이력,
      홍길동_학적부
    </React.Fragment>
  );

  const [participants, setParticipants] = useState({
    sunsuSeconds: [],
    volunteers: [],
  });

  const formatParticipant = (participant) => {
    const types = participant.types.join(",");

    let fileInfos = {};
    if (types === "자원봉사자") {
      const documentsColumn = colInfoVolunteer.find(
        (col) => col.id === "documents"
      );
      documentsColumn.columns.forEach((colInfo) => {
        const file = participant.fileInfos.find(
          (fileInfo) => colInfo.name === fileInfo.fileName
        );
        if (file) {
          fileInfos[colInfo.id] = { ...file, status: "제출완료" };
        } else {
          fileInfos[colInfo.id] = { fileName: colInfo.name, status: "미제출" };
        }
      });

      return {
        participantId: participant.participantId,
        name: participant.name,
        identityNumber: participant.identityNumber,
        types: types,
        ...fileInfos,
        "docu-allSubmit": {
          status: participant.isAllFileConfirmed ? "제출완료" : "미제출",
        },
      };
    } else {
      const documentsColumn = colInfo.find((col) => col.id === "documents");
      documentsColumn.columns.forEach((colInfo) => {
        const file = participant.fileInfos.find(
          (fileInfo) => colInfo.name === fileInfo.fileName
        );

        if (file) {
          fileInfos[colInfo.id] = { ...file, status: "제출완료" };
        } else {
          fileInfos[colInfo.id] = { fileName: colInfo.name, status: "미제출" };
        }
      });

      if (!participant.isForeigner) {
        delete fileInfos["docu5"];
      }

      return {
        participantId: participant.participantId,
        name: participant.name,
        identityNumber: participant.identityNumber,
        types: types,
        events: participant.events.join(","),
        ...fileInfos,
        "docu-allSubmit": {
          status: participant.isAllFileConfirmed ? "제출완료" : "미제출",
        },
      };
    }
  };

  const auth = useContext(AuthContext);
  const { sendRequest, setError } = useContext(HttpContext);

  // API
  const listHandler = useCallback(
    async () => {
      try {
        // const responseData = await sendRequest(
        //   `${process.env.REACT_APP_BACKEND_URL}/api/user/file`,
        //   "GET",
        //   null,
        //   {
        //     Authorization: `Bearer ${auth.token}`,
        //   },
        //   `데이터 로드 실패`
        // );

        // TODO : change Dummy DATA
        const responseData = {
          isSuccess: true,
          payload: {
            participants: [
              {
                participantId: 1,
                name: "조서영",
                isForeigner: false,
                identityNumber: "961201-0000000",
                types: ["선수", "세컨"],
                events: ["품새 개인전", "품새 단체전"],
                fileInfos: [
                  { fileId: 1, fileName: "증명사진" },
                  { fileId: 2, fileName: "학적부" },
                ],
                isAllFileConfirmed: false,
              },
              {
                participantId: 2,
                name: "조땡땡",
                isForeigner: true,
                identityNumber: null,
                types: ["선수"],
                events: ["겨루기 개인전"],
                fileInfos: [],
                isAllFileConfirmed: false,
              },
              {
                participantId: 3,
                name: "조삼삼",
                isForeigner: true,
                identityNumber: "",
                types: ["세컨"],
                events: [],
                fileInfos: [{ fileId: 3, fileName: "증명사진" }],
                isAllFileConfirmed: false,
              },
              {
                participantId: 4,
                name: "조사삼",
                isForeigner: true,
                identityNumber: "",
                types: ["자원봉사자"],
                fileInfos: [{ fileId: 4, fileName: "증명사진" }],
                isAllFileConfirmed: true,
              },
              {
                participantId: 5,
                name: "조오옹",
                isForeigner: true,
                identityNumber: "",
                types: ["자원봉사자"],
                fileInfos: [],
                isAllFileConfirmed: false,
              },
            ],
          },
        };
        // const responseData = {
        //   isSuccess: true,
        //   payload: { isParticipantExists: false },
        // };

        if (responseData.payload.participants) {
          let participantsData = {
            sunsuSeconds: [],
            volunteers: [],
          };

          responseData.payload.participants.forEach((participant) => {
            const participantData = formatParticipant(participant);
            if (participantData.types === "자원봉사자") {
              participantsData.volunteers.push(participantData);
            } else {
              participantsData.sunsuSeconds.push(participantData);
            }
          });

          setParticipants(participantsData);
        }
      } catch (err) {}
    },
    [
      // auth.token, sendRequest, formatParticipant
    ]
  );

  const submitOneFileHandler = async (file, id) => {
    const { result, message } = checkValidityFile(file);
    if (!result) {
      setError({ title: "파일정보 확인", detail: message });
      return;
    }

    const idArray = id.split("-");
    const rowNum = Number(idArray[0].replace("row", ""));
    const docuIdx = idArray[2];

    try {
      let formData = new FormData();
      formData.append("userId", auth.userId);

      const participant = participants[idArray[3]][rowNum];

      const participantSendData = {
        participantId: participant.participantId,
        fileId: participant[docuIdx].fileId,
        fileName: participant[docuIdx].fileName,
      };

      formData.append("participant", JSON.stringify(participantSendData));
      formData.append("file", file, participant[docuIdx].fileName);

      const responseData = await sendRequest(
        `${process.env.REACT_APP_BACKEND_URL}/api/user/file`,
        "POST",
        formData,
        {
          Authorization: `Bearer ${auth.token}`,
        },
        `파일 업로드 실패`
      );

      // DUMMY DATA
      // const responseData = {
      //   isSuccess: true,
      //   message: null,
      //   payload: {
      //     fileInfos: [
      //       {
      //         fileId: 100,
      //         fileName: "서약서",
      //       },
      //     ],
      //   },
      // };

      if (responseData.isSuccess) {
        let participantsData = participants;
        participantsData[idArray[3]][rowNum][docuIdx].status = "제출완료";
        participantsData[idArray[3]][rowNum][docuIdx].fileId =
          responseData.payload.fileInfos[0].fileId;
        setParticipants({ ...participantsData });
      } else {
        setError({
          title: `파일 업로드 실패`,
          detail: responseData.message,
        });
      }
    } catch (error) {}
  };

  const submitMultipleFileHandler = async (files, id) => {
    console.log(files);
    console.log(id);

    const filesArray = Array.from(files);

    filesArray.forEach((file) => {
      const { result, message } = checkValidityFileMultiple(file);
      if (!result) {
        setError({ title: "파일정보 확인", detail: message });
        return;
      }
    });

    const idArray = id.split("-");
    const rowNum = Number(idArray[0].replace("row", ""));

    try {
      //       userId
      // participant	participantId
      // 	fileInfos	fileId
      // 		fileName
      //    files
      let formData = new FormData();
      formData.append("userId", auth.userId);
      const participant = participants[idArray[3]][rowNum];
      const participantSendData = {
        participantId: participant.participantId,
        fileInfos: filesArray.map((file) => {
          const docuName = file.name.split(/_|\./)[1];
          const docuIdx = documentsColumn.columns.find(
            (col) => col.name === docuName
          ).id;
          return {
            fileId: participant[docuIdx].fileId,
            fileName: docuName,
          };
        }),
      };
      formData.append("participant", JSON.stringify(participantSendData));

      // const fileLength = filesArray.length;
      for (const file of files) {
        console.log(file); // 배열[0] ~ 끝까지 순차적 출력
        // console.log(array); // 배열 전체 출력
        formData.append("files", file, file.name.split(/_|\./)[1]);
      }
      // const responseData = await sendRequest(
      //   `${process.env.REACT_APP_BACKEND_URL}/api/user/file`,
      //   "POST",
      //   formData,
      //   {
      //     Authorization: `Bearer ${auth.token}`,
      //   },
      //   `파일 업로드 실패`
      // );
      // DUMMY DATA
      // const responseData = {
      //   isSuccess: true,
      //   message: null,
      //   payload: {
      //     fileInfos: [
      //       {
      //         fileId: 100,
      //         fileName: "서약서",
      //       },
      //     ],
      //   },
      // };
      // if (responseData.isSuccess) {
      //   let participantsData = participants;
      //   participantsData[idArray[3]][rowNum][docuIdx].status = "제출완료";
      //   participantsData[idArray[3]][rowNum][docuIdx].fileId =
      //     responseData.payload.fileInfos[0].fileId;
      //   setParticipants({ ...participantsData });
      // } else {
      //   setError({
      //     title: `파일 업로드 실패`,
      //     detail: responseData.message,
      //   });
      // }
    } catch (error) {}
  };

  useEffect(() => {
    listHandler();
  }, [listHandler]);

  return (
    <div className="docu-submit">
      <h2 className="docu-submit-title">서류제출</h2>
      <div className="docu-submit-noti">{notification}</div>
      <div className="docu-table-wrap">
        <div className="docu-table-subtitle">선수,세컨</div>
        <DocuSubmitTable
          columns={colInfo}
          data={participants.sunsuSeconds}
          onFileSubmit={submitOneFileHandler}
          onFilesSubmit={submitMultipleFileHandler}
          type="sunsuSeconds"
          showNumber
        />

        <div className="docu-table-subtitle">자원봉사자</div>
        <DocuSubmitTable
          columns={colInfoVolunteer}
          data={participants.volunteers}
          onFileSubmit={submitOneFileHandler}
          onFilesSubmit={submitMultipleFileHandler}
          type="volunteers"
          showNumber
        />
      </div>
    </div>
  );
};

export default DocuSubmit;
