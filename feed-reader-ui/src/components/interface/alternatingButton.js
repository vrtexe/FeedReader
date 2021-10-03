import { Button } from 'react-bootstrap';

/**
 * Just a button that alternates between two states
 */
export const AlternatingButton = (props) => {
  return (
    <>
      {props.alt ? (
        <Button
          className={props.classNormal}
          onClick={props.handleNormalAction}
          variant={props.normalVariant ? props.normalVariant : 'primary'}
          disabled={props.disabled}
        >
          {props.normalButtonText}
        </Button>
      ) : (
        <Button
          className={props.classAlt}
          onClick={props.handleAltAction}
          variant={props.altVariant ? props.altVariant : 'primary'}
          disabled={props.disabled}
        >
          {props.altButtonText}
        </Button>
      )}
    </>
  );
};
