import styled from "styled-components";
import { Divider, Button, Form } from "antd";
import { MissingPersonInfo } from "../components/addMisingPerson/MissingPersonInfo";

const validateMessages = {
  required: "${label} is required!",
  types: {
    email: "${label} is not a valid email!",
    number: "${label} is not a valid number!",
  },
  number: {
    range: "${label} must be between ${min} and ${max}",
  },
};

const onFinish = (values) => {
  console.log(values);
};

function AddMissingPersonPage() {
  return (
    <StAddMissingPersonPage>
      <InfoForm layout="vertical" name="nest-messages" onFinish={onFinish} validateMessages={validateMessages}>
        <Container>
          <p>실종자 정보 등록</p>
          <Divider />
          <MissingPersonInfo />
        </Container>
        <Form.Item wrapperCol={{}}>
          <Button type="primary" htmlType="submit">
            Submit
          </Button>
        </Form.Item>
      </InfoForm>
    </StAddMissingPersonPage>
  );
}
export default AddMissingPersonPage;

const StAddMissingPersonPage = styled.div`
  padding: 3rem;
`;
const InfoForm = styled(Form)`
  max-width: 45.9rem;
`;

// 전체 form의 container (흰 배경)
const Container = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: center;

  padding: 1.25rem;
  background-color: white;
`;
