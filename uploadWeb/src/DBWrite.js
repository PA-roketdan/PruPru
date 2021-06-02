import React, { useState } from "react";
import { dbService, storageService } from "./fbase";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableRow,
} from "@material-ui/core";
import "./DBWrite.css";
import TextField from "@material-ui/core/TextField";
import { v4 as uuidv4 } from "uuid";
import Button from "@material-ui/core/Button";
import logoImg from "./images/logoPruPru.png";

export default () => {
  const [solution, setSolution] = useState("");
  const [trashname, setTrashname] = useState("");
  const [no, setNo] = useState("");
  const [attachment, setAttachment] = useState("");

  const onSubmit = async (event) => {
    event.preventDefault();
    const attachmentRef = storageService.ref().child(`trashcan/${uuidv4()}`); // make ref of file
    const response = await attachmentRef.putString(attachment, "data_url");
    console.log(response);
    const attachmentUrl = await response.ref.getDownloadURL();

    const trash = {
      trashname,
      no,
      solution,
      attachmentUrl,
    };
    await dbService.collection("trashcan").add(trash);

    setTrashname("");
    setSolution("");
    setNo(0);
    setAttachment("");
    alert("새로운 쓰레기가 성공적으로 등록되었습니다.");
  };
  const onChange = (event) => {
    const {
      target: { value, name },
    } = event;

    if (name === "trashname") {
      setTrashname(value);
    } else if (name === "solution") {
      setSolution(value);
    } else if (name === "no") {
      setNo(value);
    }
  };
  const onFileChange = (event) => {
    const {
      target: { files },
    } = event;
    const theFile = files[0];
    const reader = new FileReader();
    reader.onloadend = (finishedEvent) => {
      const {
        currentTarget: { result },
      } = finishedEvent;
      setAttachment(result);
    };
    reader.readAsDataURL(theFile);
  };

  const clearAttachment = () => setAttachment(null);
  return (
    <div>
      <div className="header">
        <img src={logoImg} alt="prupru logo " trashname="푸릇푸릇" />
      </div>
      <div className="container">
        <h3 className="tablename">푸릇푸릇 DB 등록</h3>
        <form onSubmit={onSubmit}>
          <Table className="tableWrite">
            <TableHead>
              <TableRow>
                <TableCell className="tablehead">쓰레기 이름</TableCell>
                <TableCell className="tabledata">
                  <TextField
                    className="input"
                    id="outlined-basic"
                    variant="outlined"
                    name="trashname"
                    value={trashname}
                    onChange={onChange}
                    type="text"
                    placeholder="쓰레기 이름을 입력하세요."
                    maxLength={120}
                  />
                </TableCell>
              </TableRow>
              <TableRow>
                <TableCell className="tablehead">분리배출 번호</TableCell>
                <TableCell className="tabledata">
                  <TextField
                    className="input"
                    id="outlined-basic"
                    variant="outlined"
                    name="no"
                    value={no}
                    onChange={onChange}
                    type="number"
                    placeholder="분리배출 번호를 입력하세요."
                    maxLength={120}
                  />
                </TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              <TableRow>
                <TableCell className="tablehead">
                  분리배출<br></br>방법
                </TableCell>
                <TableCell className="tabledata">
                  <div>
                    <TextField
                      className="input inputcontent"
                      id="outlined-basic"
                      variant="outlined"
                      name="solution"
                      value={solution}
                      onChange={onChange}
                      type="text"
                      placeholder="분리배출 방법을 입력해주세요."
                      maxLength={400}
                    />
                  </div>
                </TableCell>
              </TableRow>
              <TableRow>
                <TableCell className="tablehead">이미지 업로드</TableCell>
                <TableCell className="tabledata">
                  <div>
                    <input
                      type="file"
                      accept="image/*"
                      onChange={onFileChange}
                    />
                  </div>
                  <div>
                    {attachment && <img src={attachment} height="200px" />}
                    <br></br>
                    <button onClick={clearAttachment}>Clear Image</button>
                  </div>
                </TableCell>
              </TableRow>
            </TableBody>
          </Table>

          <Button variant="outlined" type="submit" className="submitbtn">
            {" "}
            등록하기
          </Button>
        </form>
      </div>
    </div>
  );
};
