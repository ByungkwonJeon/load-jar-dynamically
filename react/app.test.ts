import axiosInstance from './axiosInstance';
import { fetchGet, fetchPost, fetchPut } from './your-api-file'; // adjust path

jest.mock('./axiosInstance', () => ({
  __esModule: true,
  default: {
    request: jest.fn(),
  },
}));

describe('API Service', () => {
  const mockData = { data: 'test data' };
  const headers = { Authorization: 'Bearer token' };

  beforeEach(() => {
    jest.clearAllMocks();
  });

  describe('fetchGet', () => {
    it('should call axiosInstance.request with correct config', async () => {
      (axiosInstance.request as jest.Mock).mockResolvedValue({ data: mockData });

      const endpoint = '/test-endpoint';
      const result = await fetchGet(endpoint, headers);

      expect(axiosInstance.request).toHaveBeenCalledWith({
        method: 'get',
        url: endpoint,
        data: undefined,
        headers,
        withCredentials: true,
      });
      expect(result).toEqual(mockData);
    });
  });

  describe('fetchPost', () => {
    it('should call axiosInstance.request with correct config', async () => {
      (axiosInstance.request as jest.Mock).mockResolvedValue({ data: mockData });

      const endpoint = '/test-post';
      const data = { key: 'value' };
      const result = await fetchPost(endpoint, data, headers);

      expect(axiosInstance.request).toHaveBeenCalledWith({
        method: 'post',
        url: endpoint,
        data,
        headers,
        withCredentials: true,
      });
      expect(result).toEqual(mockData);
    });
  });

  describe('fetchPut', () => {
    it('should call axiosInstance.request with correct config', async () => {
      (axiosInstance.request as jest.Mock).mockResolvedValue({ data: mockData });

      const endpoint = '/test-put';
      const data = { key: 'value' };
      const result = await fetchPut(endpoint, data, headers);

      expect(axiosInstance.request).toHaveBeenCalledWith({
        method: 'put',
        url: endpoint,
        data,
        headers,
        withCredentials: true,
      });
      expect(result).toEqual(mockData);
    });
  });
});