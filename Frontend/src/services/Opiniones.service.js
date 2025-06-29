import httpClient from "@/http-common";

// Obtener todas las notificaciones
const getPromedioPuntuacion = (token) => {
  return httpClient.get('/mongo/opiniones/promedio-puntuacion', {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });
};

const getOpinionesError = (token) => {
  return httpClient.get('/mongo/opiniones/buscar-claves', {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });
};

export default {
  getPromedioPuntuacion,
  getOpinionesError,
};
