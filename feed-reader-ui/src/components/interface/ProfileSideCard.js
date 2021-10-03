import { createUseStyles } from 'react-jss';

const useStyles = createUseStyles({
  generalInfo: {
    width: '35%',
    padding: '4%',
    backgroundColor: 'salmon',
    borderTopLeftRadius: 'inherit',
    borderBottomLeftRadius: 'inherit',
    display: 'flex',
    flexDirection: 'column',
    justifyContent: 'center',
    alignItems: 'center',
    boxSizing: 'border-box',
    fontFamily: 'inherit',
  },
  profileImageContainer: {
    width: '80%',
    aspectRatio: '1 / 1',
    boxSizing: 'border-box',
    overflow: 'hidden',
    fontFamily: 'inherit',
  },

  profileImage: {
    backgroundImage:
      'url(https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_1280.png)',
    backgroundPosition: 'center',
    backgroundSize: '100% 100%',
    width: '100%',
    height: '100%',
    borderRadius: '50%',
    overflow: 'hidden',
    fontFamily: 'inherit',
  },

  profileName: {
    height: '35%',
    boxSizing: 'border-box',
    padding: '4%',
    width: '100%',
    fontFamily: 'inherit',
    display: 'flex',
    flexDirection: 'column',
    justifyContent: 'center',
    alignItems: 'center',
    userSelect: 'none',
  },
  profileNameLabels: {
    width: '100%',
    height: '50%',
    display: 'flex',
    flexDirection: 'row',
    justifyContent: 'space-around',
    boxSizing: 'border-box',
    padding: '0 0.5em',
    fontSize: '1.5vw',
    fontWeight: 'bold',
    fontFamily: 'inherit',
    userSelect: 'none',
  },

  profileNameValues: {
    width: '100%',
    height: '50%',
    display: 'flex',
    flexDirection: 'row',
    justifyContent: 'space-between',
    boxSizing: 'border-box',
    fontFamily: 'inherit',
    fontWeight: 'bold',
    color: 'white',
    fontSize: '1.5vw',
    userSelect: 'none',
  },
  subscriptionInfo: {
    width: '100%',
    height: '20%',
    display: 'flex',
    flexDirection: 'row',
    justifyContent: 'center',
    userSelect: 'none',
    fontWeight: 'bolder',
  },
  spanTextCenter: {
    display: 'flex',
    flexDirection: 'row',
    justifyContent: 'center',
    alignItems: 'center',
  },
});

/**
 * The right card of the profile providing the general information
 */
const ProfileSideCard = (props) => {
  const classes = useStyles();
  return (
    <>
      <div className={classes.generalInfo}>
        <div className={classes.profileImageContainer}>
          <div className={classes.profileImage}></div>
        </div>
        <div className={classes.profileName}>
          <div className={classes.profileNameLabels}>
            <span className={classes.spanTextCenter}>
              {props.fieldOneLabel}
            </span>
            <span className={classes.spanTextCenter}>
              {props.fieldTwoLabel}
            </span>
          </div>
          <div className={classes.profileNameValues}>
            <span className={classes.spanTextCenter}>
              {props.fieldOneValue}
            </span>
            <span className={classes.spanTextCenter}>
              {props.fieldTwoValue}
            </span>
          </div>
        </div>
        <div className={classes.subscriptionInfo}>{props.children}</div>
      </div>
    </>
  );
};

export default ProfileSideCard;
