export default {

  'GET /api/user/current': (req, res) => {
    setTimeout(() => {
      res.send({
        message: '',
        object: {
          id: 1,
          username: null,
        },
      });
    }, 1000);
  },

  'POST /api/user/login': (req, res) => {
    setTimeout(() => {
      res.send({ message: '', object: {} });
    }, 1000);
  },
};
