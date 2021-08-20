export default {

  'GET /api/user/current': (req, res) => {
    setTimeout(() => {
      res.send({
        message: '',
        object: {
          username: 'admin',
          authorities: [
            { authority: 'ROLE_ADMIN' },
          ],
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
