export const AlternatingText = (props) => {
  return (
    <>
      {props.alt ? (
        <span className={props.classNormal}>{props.normalText}</span>
      ) : (
        <span className={props.classAlt}>{props.altText}</span>
      )}
    </>
  );
};