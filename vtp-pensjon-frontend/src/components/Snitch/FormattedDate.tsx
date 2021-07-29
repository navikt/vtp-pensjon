import * as React from "react";
import { DateTimeFormatter, LocalDateTime } from "@js-joda/core";

const dateFormatter = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");

interface FormattedDateProps {
  time: string;
}
export default function FormattedDate(props: FormattedDateProps): JSX.Element {
  const time = LocalDateTime.parse(props.time).format(dateFormatter);
  return <>{time}</>;
}
