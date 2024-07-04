
import styles from './style.module.css';
import image from './bg.jpeg';

const Banner = () => {
  return (
    <div className={styles.banner}>
      <img className={styles.img} src={image} alt="banner" />
      <div className={styles.text}>
        <div className={styles.title}>南方科技大学活动中心</div>
        <div className={styles.description}>SUSTech Entertainment Center</div>
      </div>
    </div>
  );
}

export default Banner;
