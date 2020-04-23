
export default {

  'GET /api/user/current': (req, res) => {
    setTimeout(() => {
      res.send([{
        userId: 1,
      }]);
    }, 1000);
  },

  'POST /api/user/login': (req, res) => {
    setTimeout(() => {
      res.send({ success: true, message: '', object: {} });
    }, 1000);
  },
};
