import Loader from 'react-loader-spinner';
import { createUseStyles } from 'react-jss';

const useStyles = createUseStyles({
  loader: {
    width: '100%',
    height: '100%',
    display: 'flex',
    justifyContent: 'center',
    alignItems: 'center',
  },
});

const LoadingComponent = () => {
  const classes = useStyles();

  return (
    <>
      <div className={classes.loader}>
        <Loader
          type="Puff"
          color="lightgray"
          height="45"
          width="45"
          visible="true"
        />
      </div>
    </>
  );
};

export default LoadingComponent;
