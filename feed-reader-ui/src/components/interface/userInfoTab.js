import { createUseStyles } from 'react-jss';

const useStyles = createUseStyles({
  informationTab: {
    display: 'flex',
    flexDirection: 'column',
    boxSizing: 'border-box',
    height: '40%',
  },

  tabTitle: {
    boxSizing: 'border-box',
    padding: '2% 0',
    borderBottom: '0.1em solid lightgray',
    fontWeight: 'bold',
    fontFamily: 'inherit',
    display: 'flex',
    flexDirection: 'row',
    justifyContent: 'start',
    alignItems: 'center',
    fontSize: '2vw',
    width: '100%',
    height: '30%',
    marginBottom: '2%',
  },

  fieldLabels: {
    display: 'flex',
    justifyContent: 'space-evenly',
    marginBottom: '2%',
    fontSize: '1.9vw',
    fontWeight: 'bolder',
  },

  fieldValues: {
    display: 'flex',
    justifyContent: 'space-evenly',
    color: 'SlateGray',
    fontSize: '1.5vw',
  },

  spanTextInfo: {
    width: '50%',
    display: 'flex',
    justifyContent: 'start',
    alignItems: 'center',
  },

  tabContent: {
    display: 'flex',
    flexDirection: 'column',
    justifyContent: 'center',
    boxSizing: 'border-box',
    padding: '0 3%',
    height: '70%',
  },
});

const UserInfoTab = (props) => {
  const classes = useStyles();

  return (
    <>
      {props.altContent ? (
        <div className={classes.informationTab}>{props.children}</div>
      ) : (
        <div className={classes.informationTab}>
          <div className={classes.tabTitle}>{props.tabTitle}</div>
          <div className={classes.tabContent}>
            <div className={classes.fieldLabels}>
              <span className={classes.spanTextInfo}>
                {props.fieldOneLabel}
              </span>
              <span className={classes.spanTextInfo}>
                {props.fieldTwoLabel}
              </span>
            </div>
            <div className={classes.fieldValues}>
              <span className={classes.spanTextInfo}>
                {props.fieldOneValue}
              </span>
              <span className={classes.spanTextInfo}>
                {props.fieldTwoValue}
              </span>
            </div>
          </div>
        </div>
      )}
    </>
  );
};

export default UserInfoTab;
