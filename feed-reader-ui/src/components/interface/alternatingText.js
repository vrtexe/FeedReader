/**
 * Just two text fields that alternate based on a value provided by a parent
 */
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
